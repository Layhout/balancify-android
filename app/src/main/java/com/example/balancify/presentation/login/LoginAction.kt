package com.example.balancify.presentation.login

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
}