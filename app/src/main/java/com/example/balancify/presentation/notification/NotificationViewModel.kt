package com.example.balancify.presentation.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.balancify.domain.model.UserModel
import com.example.balancify.domain.use_case.notification.NotificationUseCases
import com.example.balancify.domain.use_case.user.UserUseCases
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val useCases: NotificationUseCases,
    private val userUseCases: UserUseCases,
) : ViewModel() {
    private val _state = MutableStateFlow(NotificationState())

    val state = _state
        .onStart { loadData() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = NotificationState()
        )

    private val _events = Channel<NotificationEvent>()
    val events = _events.receiveAsFlow()

    private fun alertError(message: String?) {
        _events.trySend(
            NotificationEvent.OnError(message ?: "Unknown error")
        )
    }

    private fun loadData(lastDoc: DocumentSnapshot? = null) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val result = useCases.getNotifications(lastDoc)
            if (result.isFailure) {
                alertError(result.exceptionOrNull()?.message)
                return@launch
            }

            var localUser: UserModel? = _state.value.localUser

            if (localUser == null) {
                val result = userUseCases.getLocalUser()

                if (result.isSuccess) {
                    localUser = result.getOrNull()
                }
            }

            _state.update {
                it.copy(
                    notifications = if (lastDoc != null) it.notifications + (result.getOrNull()?.data
                        ?: emptyList()) else (result.getOrNull()?.data ?: emptyList()),
                    canLoadMore = result.getOrNull()!!.canLoadMore,
                    isLoading = false,
                    lastDoc = result.getOrNull()!!.lastDoc,
                    isRefreshing = false,
                    localUser = localUser,
                )
            }

            readNotification()
        }
    }

    private fun readNotification() {
        val unreadNotification = _state.value.notifications
            .filter { it.isUnread(_state.value.localUser?.id ?: "") }
            .map { it.id }

        if (unreadNotification.isEmpty()) return

        viewModelScope.launch {
            useCases.readNotification(
                _state.value.notifications
                    .filter { it.isUnread(_state.value.localUser?.id ?: "") }
                    .map { it.id }
            )
        }
    }

    fun onAction(action: NotificationAction) {
        when (action) {
            is NotificationAction.OnRefresh -> {
                loadData()
            }

            is NotificationAction.OnLoadMore -> {
                loadData(_state.value.lastDoc)
            }
        }
    }
}