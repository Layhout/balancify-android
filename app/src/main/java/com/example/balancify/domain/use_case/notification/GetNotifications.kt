package com.example.balancify.domain.use_case.notification

import com.example.balancify.domain.model.NotificationModel
import com.example.balancify.domain.repository.NotificationRepository
import com.example.balancify.service.PaginatedData
import com.google.firebase.firestore.DocumentSnapshot

class GetNotifications(
    private val repository: NotificationRepository,
) {
    suspend operator fun invoke(
        lastDoc: DocumentSnapshot?
    ): Result<PaginatedData<NotificationModel>> =
        repository.getAllNotification(lastDoc)

}