package com.example.balancify.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.balancify.core.constant.GlobalAppStateFlag
import com.example.balancify.core.manager.GlobalAppStateManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class HomeViewModel(
    private val globalAppStateManager: GlobalAppStateManager
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())

    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeState()
    )

    private val _events = Channel<HomeEvent>()
    val events = _events.receiveAsFlow()

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