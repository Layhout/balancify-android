package com.macrobytes.balancify.presentation.home.component.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.macrobytes.balancify.domain.model.UserModel
import com.macrobytes.balancify.domain.use_case.dashboard.DashboardUseCases
import com.macrobytes.balancify.domain.use_case.user.UserUseCases
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val dashboardUseCases: DashboardUseCases,
    private val userUseCases: UserUseCases,
) : ViewModel() {
    private val _state = MutableStateFlow(DashboardState())
    val state = _state.onStart { loadData() }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardState()
    )

    private val _events = Channel<DashboardEvent>()
    val events = _events.receiveAsFlow()

    private fun alertError(message: String?) {
        _events.trySend(
            DashboardEvent.OnError(message ?: "Unknown error")
        )
    }

    private fun loadData(isLoading: Boolean = true) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = isLoading) }

            val result = dashboardUseCases.getDashboardData()
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
                    isLoading = false,
                    data = result.getOrNull()!!,
                    localUser = localUser,
                    isRefreshing = false
                )
            }
        }
    }

    fun onAction(event: DashboardAction) {
        when (event) {
            is DashboardAction.OnRefresh -> {
                _state.update { it.copy(isRefreshing = true) }
                loadData(false)
            }
        }
    }
}