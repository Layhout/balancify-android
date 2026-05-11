package com.macrobytes.balancify.data.data_source.group

import com.macrobytes.balancify.domain.model.GroupMetadataModel
import com.macrobytes.balancify.domain.model.GroupModel
import com.macrobytes.balancify.service.PaginatedData
import com.google.firebase.firestore.DocumentSnapshot

interface GroupRemoteDataSource {
    suspend fun createGroup(group: GroupModel, groupMetadata: GroupMetadataModel)
    suspend fun getGroupsWithUser(
        lastDoc: DocumentSnapshot?,
        id: String,
        search: String?
    ): PaginatedData<GroupModel>

    suspend fun getGroupById(id: String, userId: String): GroupModel
    suspend fun leaveGroup(
        id: String,
        group: GroupModel,
        groupMetadata: GroupMetadataModel,
    )

    suspend fun deleteGroup(id: String)
    suspend fun updateGroup(
        id: String,
        group: GroupModel,
        groupMetadata: GroupMetadataModel,
    )
}