package com.macrobytes.balancify.presentation.group_detail

sealed interface GroupDetailEvent {
    data class OnError(val message: String) : GroupDetailEvent
    data object OnLeaveGroup : GroupDetailEvent
}