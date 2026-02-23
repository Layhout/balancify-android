package com.example.balancify.data.data_source.group

import com.example.balancify.core.constant.BatchSetItem
import com.example.balancify.core.constant.PaginatedData
import com.example.balancify.domain.model.GroupMetadataModel
import com.example.balancify.domain.model.GroupModel
import com.example.balancify.service.DatabaseService

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

    override suspend fun getGroups(): PaginatedData<GroupModel> {
        TODO("Not yet implemented")
    }
}