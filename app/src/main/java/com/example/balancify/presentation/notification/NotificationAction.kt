package com.example.balancify.presentation.notification

sealed interface NotificationAction {
    data object OnRefresh : NotificationAction
    data object OnLoadMore : NotificationAction
}