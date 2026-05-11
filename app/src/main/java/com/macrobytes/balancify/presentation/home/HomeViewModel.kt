package com.macrobytes.balancify.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.macrobytes.balancify.core.constant.GlobalAppStateFlag
import com.macrobytes.balancify.core.manager.GlobalAppStateManager
import com.macrobytes.balancify.domain.use_case.notification.NotificationUseCases
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val notificationUseCases: NotificationUseCases,
    private val globalAppStateManager: GlobalAppStateManager
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())

    val state = _state.onStart {
        checkNotification()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeState()
    )

    private val _events = Channel<HomeEvent>()
    val events = _events.receiveAsFlow()

    private fun checkNotification() {
        viewModelScope.launch {
            val result = notificationUseCases.checkUnreadNotification()
            if (result.isSuccess) {
                _state.update {
                    it.copy(
                        hasUnreadNotification = result.getOrNull()!!
                    )
                }
            }
        }
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnToggleFabClick -> {
                _state.update {
                    it.copy(
                        toggleFab = !it.toggleFab
                    )
                }
            }

            is HomeAction.OnCollectFlag -> {
                /// Do not pull flag because sub screen need the flag
                val groupRefreshFlag = globalAppStateManager.getFlag(
                    GlobalAppStateFlag.GROUP_LIST_SHOULD_REFRESH
                )

                /// Do not pull flag because sub screen need the flag
                val expenseRefreshFlag = globalAppStateManager.getFlag(
                    GlobalAppStateFlag.EXPENSE_LIST_SHOULD_REFRESH
                )

                if (groupRefreshFlag) {
                    _events.trySend(HomeEvent.OnRefreshGroup)
                }

                if (expenseRefreshFlag) {
                    _events.trySend(HomeEvent.OnRefreshExpense)
                }
            }
        }
    }

}