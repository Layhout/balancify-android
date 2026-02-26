package com.example.balancify.data.data_source.group

import com.example.balancify.core.constant.BatchSetItem
import com.example.balancify.core.constant.ITEMS_LIMIT
import com.example.balancify.core.constant.PaginatedData
import com.example.balancify.domain.model.GroupMetadataModel
import com.example.balancify.domain.model.GroupModel
import com.example.balancify.service.DatabaseService
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath.documentId
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject

class GroupRemoteDataSourceImp(
    private val db: DatabaseService,
) : GroupRemoteDataSource {
    private val collectionName: String = "groups"
    private val metaDataCollectionName: String = "group_metadata"

    override suspend fun create(group: GroupModel, groupMetadata: GroupMetadataModel) {
        db.batchSet(
            listOf(
                BatchSetItem(
                    collection = collectionName,
                    id = group.id,
                    data = group,
                ),
                BatchSetItem(
                    collection = metaDataCollectionName,
                    id = group.id,
                    data = groupMetadata
                ),
            )
        )
    }

    override suspend fun getGroupsWithUser(
        lastDoc: DocumentSnapshot?,
        id: String
    ): PaginatedData<GroupModel> {
        val result = db.getPage(collectionName, ITEMS_LIMIT, lastDoc, queryBuilder = {
            it.whereArrayContains("memberIds", id)
                .orderBy("createdAt", Query.Direction.DESCENDING)
        })

        val groups = result.snapshot.documents.mapNotNull {
            it.toObject<GroupModel>()
        }

        val canLoadMore = result.canLoadMore

        return PaginatedData(
            data = groups,
            canLoadMore = canLoadMore,
            lastDoc = result.snapshot.documents.lastOrNull()
        )
    }

    override suspend fun getGroupById(id: String, userId: String): GroupModel {
        val result = db.getPage(collectionName, 1, null, queryBuilder = {
            it.whereEqualTo(documentId(), id)
                .whereArrayContains("memberIds", userId)
        })

        val group = result.snapshot.documents.firstOrNull()?.toObject<GroupModel>()

        return group ?: throw Exception("Group not found")
    }
}