package com.example.balancify.domain.use_case.notification

import com.example.balancify.domain.repository.NotificationRepository

class ReadNotification(
    private val repository: NotificationRepository,
) {
    suspend operator fun invoke(ids: List<String>) = repository.readNotification(ids)
}