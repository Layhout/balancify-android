package com.example.balancify.data.data_source.notification

import com.example.balancify.domain.model.NotificationModel
import com.example.balancify.service.PaginatedData
import com.google.firebase.firestore.DocumentSnapshot

interface NotificationRemoteDataSource {
    suspend fun createNotification(notification: NotificationModel)
    suspend fun getUnreadNotis(): Boolean
    suspend fun readNotification(ids: List<String>)
    suspend fun getAllNotification(lastDoc: DocumentSnapshot?): PaginatedData<NotificationModel>
}