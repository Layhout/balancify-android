package com.macrobytes.balancify.presentation.login

import android.content.Context

sealed interface LoginAction {
    data class OnScreenLoad(
        val context: Context,
        val onSuccess: () -> Unit,
    ) : LoginAction

    data class OnSignInClick(
        val context: Context,
        val onSuccess: () -> Unit
    ) : LoginAction

    data object OnLoginFormBottomSheetToggle : LoginAction
    data object OnResetPasswordDialogToggle : LoginAction
    data class OnIsSignUp(val value: Boolean) : LoginAction
    data class OnEmailChange(val value: String) : LoginAction
    data class OnPasswordChange(val value: String) : LoginAction
    data class OnResetPasswordEmailChange(val value: String) : LoginAction
    data object OnSubmitLoginForm : LoginAction
    data class OnResetPasswordClick(val context: Context) : LoginAction
}