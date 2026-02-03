package com.example.balancify.presentation.friend

sealed interface FriendAction {
    data object OnAddFriendClick : FriendAction
    data object OnLoadMore : FriendAction
    data object OnRefresh : FriendAction
}