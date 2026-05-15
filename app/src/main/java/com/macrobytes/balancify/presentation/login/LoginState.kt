package com.macrobytes.balancify.presentation.login

data class LoginState(
    val isLoading: Boolean = false,
    val showLoginFormBottomSheet: Boolean = false,
    val isSignUp: Boolean = false,
    val isEnableAllAction: Boolean = true,
    val isEmailInvalid: Boolean = false,
    val isResetPasswordEmailInvalid: Boolean = false,
    val isPasswordInvalid: Boolean = false,
    val showResetPasswordDialog: Boolean = false,
    val email: String = "",
    val password: String = "",
    val resetPasswordEmail: String = "",
)
