package com.macrobytes.balancify.domain.repository

import com.macrobytes.balancify.domain.model.NotificationModel
import com.macrobytes.balancify.service.PaginatedData
import com.google.firebase.firestore.DocumentSnapshot

interface NotificationRepository {
    suspend fun createNotification(notification: NotificationModel): Result<Unit>
    suspend fun getUnreadNotis(): Result<Boolean>
    suspend fun readNotification(ids: List<String>): Result<Unit>
    suspend fun getAllNotification(lastDoc: DocumentSnapshot?): Result<PaginatedData<NotificationModel>>
}