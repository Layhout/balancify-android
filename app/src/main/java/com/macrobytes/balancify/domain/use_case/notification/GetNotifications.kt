package com.macrobytes.balancify.domain.use_case.notification

import com.macrobytes.balancify.domain.model.NotificationModel
import com.macrobytes.balancify.domain.repository.NotificationRepository
import com.macrobytes.balancify.service.PaginatedData
import com.google.firebase.firestore.DocumentSnapshot

class GetNotifications(
    private val repository: NotificationRepository,
) {
    suspend operator fun invoke(
        lastDoc: DocumentSnapshot?
    ): Result<PaginatedData<NotificationModel>> =
        repository.getAllNotification(lastDoc)

}