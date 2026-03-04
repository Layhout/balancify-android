package com.example.balancify.data.data_source.group

import com.example.balancify.domain.model.GroupMetadataModel
import com.example.balancify.domain.model.GroupModel
import com.example.balancify.service.PaginatedData
import com.google.firebase.firestore.DocumentSnapshot

interface GroupRemoteDataSource {
    suspend fun createGroup(group: GroupModel, groupMetadata: GroupMetadataModel)
    suspend fun getGroupsWithUser(lastDoc: DocumentSnapshot?, id: String): PaginatedData<GroupModel>
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