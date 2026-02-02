package com.example.balancify.presentation.home.component.account

import android.content.Context

sealed interface AccountAction {
    data object OnLogoutClick : AccountAction
    data object OnLogoutDismiss : AccountAction
    data class OnLogoutConfirmClick(val context: Context) : AccountAction
    data object OnFriendClick : AccountAction
}