package com.macrobytes.balancify.presentation.friend

sealed interface FriendEvent {
    data class OnError(val message: String) : FriendEvent
    data object OnShareLinkClicked : FriendEvent
}