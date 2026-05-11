package com.macrobytes.balancify.presentation.notification

sealed interface NotificationEvent {
    data class OnError(val message: String) : NotificationEvent
}