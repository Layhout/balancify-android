package com.example.balancify.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.balancify.core.constant.RepositoryResult
import com.example.balancify.domain.model.UserModel
import com.example.balancify.domain.repository.UserRepository
import com.example.balancify.service.AuthResult
import com.example.balancify.service.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authService: AuthService,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())

    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = LoginState()
        )

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnScreenLoad -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    processSignInResult(
                        authService.signInWithBottomSheet(action.context),
                        action.onSuccess
                    )
                    _state.update { it.copy(isLoading = false) }
                }
            }

            is LoginAction.OnSignInClick -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    processSignInResult(
                        authService.signInWithDialog(action.context),
                        action.onSuccess
                    )
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    private suspend fun processSignInResult(result: AuthResult, onSuccess: () -> Unit) {
        var user: UserModel? = result.user?.also {
            if (result.isNewUser) {
                userRepository.addUser(it)
            }
        }

        if (user == null) {
            val result = userRepository.getUser(result.userId ?: "")

            if (result is RepositoryResult.Success) {
                user = result.data
            }
        }

        user?.let {
            userRepository.addLocalUser(it)
        }

        if (result.successful) {
            onSuccess()
        }
    }
}
