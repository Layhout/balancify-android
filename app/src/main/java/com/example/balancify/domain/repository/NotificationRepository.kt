package com.example.balancify.domain.repository

import com.example.balancify.domain.model.NotificationModel
import com.example.balancify.service.PaginatedData
import com.google.firebase.firestore.DocumentSnapshot

interface NotificationRepository {
    suspend fun createNotification(notification: NotificationModel): Result<Unit>
    suspend fun getUnreadNotis(): Result<Boolean>
    suspend fun readNotification(ids: List<String>): Result<Unit>
    suspend fun getAllNotification(lastDoc: DocumentSnapshot?): Result<PaginatedData<NotificationModel>>
}