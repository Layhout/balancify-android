package com.example.balancify.presentation.group_detail

sealed interface GroupDetailEvent {
    data class OnError(val message: String) : GroupDetailEvent
}