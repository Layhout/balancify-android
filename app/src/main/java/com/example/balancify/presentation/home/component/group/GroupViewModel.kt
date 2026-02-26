package com.example.balancify.presentation.home.component.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.balancify.domain.use_case.group.GroupUseCases
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GroupViewModel(
    private val groupUseCases: GroupUseCases
) : ViewModel() {
    private val _state = MutableStateFlow(GroupState())
    val state = _state.onStart {
        loadData()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = GroupState()
    )

    private val _events = Channel<GroupEvent>()
    val events = _events.receiveAsFlow()

    private fun alertError(message: String?) {
        _events.trySend(
            GroupEvent.OnError(message ?: "Unknown error")
        )
    }

    private fun loadData(lastDoc: DocumentSnapshot? = null, isLoading: Boolean = true) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = isLoading) }

            val result = groupUseCases.getGroups(lastDoc)

            if (result.isSuccess) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        groups = if (lastDoc != null) it.groups + (result.getOrNull()?.data
                            ?: emptyList()) else (result.getOrNull()?.data ?: emptyList()),
                        canLoadMore = result.getOrNull()?.canLoadMore ?: false,
                        lastDoc = result.getOrNull()?.lastDoc,
                    )
                }
            } else {
                alertError(result.exceptionOrNull()?.message)
            }
        }
    }

    fun onAction(action: GroupAction) {
        when (action) {
            GroupAction.OnGroupClick -> TODO()
            GroupAction.OnLoadMore -> loadData(_state.value.lastDoc)
            GroupAction.OnRefresh -> {
                _state.update {
                    it.copy(
                        isRefreshing = true,
                    )
                }
                loadData(isLoading = false)
            }
        }
    }
}