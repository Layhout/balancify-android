package com.macrobytes.balancify.presentation.home.component.account

import android.content.Context

sealed interface AccountAction {
    data class OnLogoutConfirmClick(val context: Context) : AccountAction
    data class OnDeleteAccountConfirmClick(val context: Context) : AccountAction
    data object OnFriendClick : AccountAction
    data object OnDevBlogClick : AccountAction
    data object OnLogoutBottomSheetToggle : AccountAction
    data object OnDeleteAccountBottomSheetToggle : AccountAction
}