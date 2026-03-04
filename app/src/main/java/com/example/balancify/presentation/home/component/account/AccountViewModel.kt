package com.example.balancify.presentation.home.component.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.balancify.domain.use_case.user.UserUseCases
import com.example.balancify.service.AuthService
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountViewModel(
    private val userUseCases: UserUseCases,
    private val authService: AuthService,
) : ViewModel() {
    private val _state = MutableStateFlow(AccountState())
    val state = _state.onStart { loadData() }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = AccountState()
    )

    private val _events = Channel<AccountEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: AccountAction) {
        when (action) {
            is AccountAction.OnLogoutClick -> {
                _state.update { it.copy(isLogoutBottomSheetVisible = true) }
            }

            is AccountAction.OnLogoutDismiss -> {
                _state.update { it.copy(isLogoutBottomSheetVisible = false) }
            }

            is AccountAction.OnLogoutConfirmClick -> {
                _state.update { it.copy(isLogoutBottomSheetVisible = false) }

                viewModelScope.launch {
                    val result = authService.signOut(action.context)
                    if (result) {
                        _events.send(AccountEvent.OnLogoutSuccessful)
                    } else {
                        _events.send(AccountEvent.OnLogoutError("Couldn't logout"))
                    }
                }
            }

            is AccountAction.OnFriendClick -> {
                viewModelScope.launch { _events.send(AccountEvent.OnNavigateToFriend) }
            }

            is AccountAction.OnDevBlogClick -> {
                viewModelScope.launch { _events.send(AccountEvent.OnNavigateToDevBlog) }
            }
        }
    }


    private fun loadData() {
        viewModelScope.launch {
            val result = userUseCases.getLocalUser()
            if (result.isSuccess) {
                _state.update { it.copy(user = result.getOrNull()) }
            }
        }
    }

}