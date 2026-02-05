package com.example.balancify.presentation.friend

interface FriendEvent {
    data class OnError(val message: String) : FriendEvent
}