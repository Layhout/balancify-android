package com.macrobytes.balancify.presentation.notification

sealed interface NotificationAction {
    data object OnRefresh : NotificationAction
    data object OnLoadMore : NotificationAction
}