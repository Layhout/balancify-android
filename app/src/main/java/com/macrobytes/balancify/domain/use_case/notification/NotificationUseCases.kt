package com.macrobytes.balancify.domain.use_case.notification

data class NotificationUseCases(
    val getNotifications: GetNotifications,
    val checkUnreadNotification: CheckUnreadNotification,
    val readNotification: ReadNotification
)
