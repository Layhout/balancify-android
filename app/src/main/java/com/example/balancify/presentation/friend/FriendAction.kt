package com.example.balancify.presentation.friend

import androidx.compose.ui.platform.Clipboard

sealed interface FriendAction {
    data object OnAddFriendClick : FriendAction
    data object OnAddFriendConfirmClick : FriendAction
    data object OnLoadMore : FriendAction
    data object OnRefresh : FriendAction
    data class OnUnfriendClick(val id: String) : FriendAction
    data class OnAcceptFriendClick(val id: String) : FriendAction
    data class OnRejectFriendClick(val id: String) : FriendAction
    data object OnConfirmUnfriendClick : FriendAction
    data object OnDismissUnfriendClick : FriendAction
    data object OnDismissAddFriendDialog : FriendAction
    data class OnEmailUpdate(val email: String) : FriendAction
    data object OnDismissInviteLinkBottomSheet : FriendAction
    data class OnInviteLinkClick(val clipboard: Clipboard) : FriendAction
    data object OnShareLinkClick : FriendAction
}