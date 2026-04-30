package com.example.balancify.data.repository

import com.example.balancify.data.data_source.notification.NotificationRemoteDataSource
import com.example.balancify.domain.model.NotificationModel
import com.example.balancify.domain.repository.NotificationRepository
import com.example.balancify.service.PaginatedData
import com.google.firebase.firestore.DocumentSnapshot

class NotificationRepositoryImp(
    private val remoteDataSource: NotificationRemoteDataSource
) : NotificationRepository {
    override suspend fun createNotification(notification: NotificationModel): Result<Unit> {
        return Result.runCatching { remoteDataSource.createNotification(notification) }
    }

    override suspend fun getUnreadNotis(): Result<Boolean> {
        return Result.runCatching { remoteDataSource.getUnreadNotis() }
    }

    override suspend fun readNotification(ids: List<String>): Result<Unit> {
        return Result.runCatching { remoteDataSource.readNotification(ids) }
    }

    override suspend fun getAllNotification(
        lastDoc: DocumentSnapshot?
    ): Result<PaginatedData<NotificationModel>> {
        return Result.runCatching { remoteDataSource.getAllNotification(lastDoc) }
    }
}