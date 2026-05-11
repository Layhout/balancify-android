package com.macrobytes.balancify.domain.use_case.notification

import com.macrobytes.balancify.domain.repository.NotificationRepository

class CheckUnreadNotification(
    private val repository: NotificationRepository,
) {
    suspend operator fun invoke(): Result<Boolean> = repository.getUnreadNotis()
}