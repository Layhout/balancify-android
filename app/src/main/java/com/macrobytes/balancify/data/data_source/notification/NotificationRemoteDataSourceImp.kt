package com.macrobytes.balancify.data.data_source.notification

import com.macrobytes.balancify.core.constant.FirebaseCollectionName
import com.macrobytes.balancify.core.constant.ITEMS_LIMIT
import com.macrobytes.balancify.domain.model.NotificationModel
import com.macrobytes.balancify.service.AuthService
import com.macrobytes.balancify.service.BatchUpdateItem
import com.macrobytes.balancify.service.DatabaseService
import com.macrobytes.balancify.service.PaginatedData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject

class NotificationRemoteDataSourceImp(
    private val db: DatabaseService,
    private val auth: AuthService,
) : NotificationRemoteDataSource {
    private val collectionName: String = FirebaseCollectionName.NOTIFICATION.value

    override suspend fun createNotification(notification: NotificationModel) {
        db.setData(collectionName, notification.id, notification)
    }

    override suspend fun getUnreadNotis(): Boolean {
        val result = db.getPage(
            collectionName, 1, null,
            queryBuilder = {
                it.whereEqualTo("userReadFlag.${auth.userId}", false)
            }
        )

        val notifications = result.snapshot.documents.mapNotNull {
            it.toObject<NotificationModel>()
        }

        return notifications.isNotEmpty()
    }

    override suspend fun readNotification(ids: List<String>) {
        db.batchUpdate(
            ids.map {
                BatchUpdateItem(
                    collection = collectionName,
                    id = it,
                    fields = mapOf(
                        auth.userId to true,
                    )
                )
            }
        )
    }

    override suspend fun getAllNotification(lastDoc: DocumentSnapshot?): PaginatedData<NotificationModel> {
        val result = db.getPage(
            collectionName, ITEMS_LIMIT, lastDoc,
            queryBuilder = {
                it.whereArrayContains("ownerIds", auth.userId).orderBy(
                    "createdAt",
                    Query.Direction.DESCENDING
                )
            }
        )

        val notifications = result.snapshot.documents.mapNotNull {
            it.toObject<NotificationModel>()
        }

        val canLoadMore = result.canLoadMore

        return PaginatedData(
            data = notifications,
            canLoadMore = canLoadMore,
            lastDoc = result.snapshot.documents.lastOrNull()
        )
    }
}