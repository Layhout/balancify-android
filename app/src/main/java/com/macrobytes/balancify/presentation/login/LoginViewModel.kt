package com.macrobytes.balancify.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.macrobytes.balancify.domain.model.UserModel
import com.macrobytes.balancify.domain.use_case.user.UserUseCases
import com.macrobytes.balancify.service.AuthService
import com.macrobytes.balancify.service.AuthServiceResponse
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authService: AuthService,
    private val userUseCases: UserUseCases,
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())

    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = LoginState()
        )

    private val _events = Channel<LoginEvent>()
    val events = _events.receiveAsFlow()

    private fun alertError(message: String?) {
        _events.trySend(
            LoginEvent.OnError(message ?: "Unknown error")
        )
    }

    private fun isValidEmail(email: String): Boolean {
        if (email.isNotEmpty()) {
            // Email is considered erroneous until it completely matches EMAIL_ADDRESS.
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        return false
    }

    private fun isStrongPassword(password: String): Boolean {
        val passwordRegex =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$".toRegex()
        return password.matches(passwordRegex)
    }

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnScreenLoad -> {
//                viewModelScope.launch {
//                    _state.update { it.copy(isLoading = true) }
//                    processSignInResult(
//                        authService.signInWithBottomSheet(action.context),
//                        action.onSuccess
//                    )
//                    _state.update { it.copy(isLoading = false) }
//                }
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

            is LoginAction.OnLoginFormBottomSheetToggle -> {
                _state.update {
                    it.copy(
                        showLoginFormBottomSheet = !it.showLoginFormBottomSheet
                    )
                }
            }

            is LoginAction.OnIsSignUp -> {
                _state.update {
                    it.copy(
                        isSignUp = action.value
                    )
                }
            }

            is LoginAction.OnEmailChange -> {
                _state.update {
                    it.copy(
                        email = action.value
                    )
                }
            }

            is LoginAction.OnPasswordChange -> {
                _state.update {
                    it.copy(
                        password = action.value
                    )
                }
            }

            is LoginAction.OnResetPasswordEmailChange -> {
                _state.update {
                    it.copy(
                        resetPasswordEmail = action.value
                    )
                }
            }

            is LoginAction.OnSubmitLoginForm -> {}
            is LoginAction.OnResetPasswordDialogToggle -> {
                _state.update {
                    it.copy(
                        showResetPasswordDialog = !it.showResetPasswordDialog
                    )
                }
            }

            is LoginAction.OnResetPasswordClick -> {
                if (!isValidEmail(_state.value.resetPasswordEmail)) {
                    _state.update {
                        it.copy(
                            isResetPasswordEmailInvalid = true,
                        )
                    }

                    return
                }
                viewModelScope.launch {
                    val result = authService.sendResetPasswordEmail(
                        action.context,
                        _state.value.resetPasswordEmail
                    )

                    if (result.successful) {
                        alertError("Sent!")
                        _state.update {
                            it.copy(
                                showResetPasswordDialog = false,
                                resetPasswordEmail = "",
                            )
                        }
                    }
                }
            }
        }
    }

    private suspend fun processSignInResult(result: AuthServiceResponse, onSuccess: () -> Unit) {
        var user: UserModel? = result.user?.also {
            if (result.isNewUser) {
                userUseCases.addUser(it)
            }
        }

        if (user == null) {
            val result = userUseCases.getUser(result.userId ?: "")

            if (result.isSuccess) {
                user = result.getOrNull()
            }
        }

        user?.let {
            userUseCases.addLocalUser(it)
        }

        if (result.successful) {
            onSuccess()
        }
    }
}
