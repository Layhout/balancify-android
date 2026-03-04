package com.example.balancify.presentation.home.component.group

sealed interface GroupEvent {
    data class OnError(val message: String) : GroupEvent
}