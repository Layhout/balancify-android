package com.example.balancify.data.data_source.group

import com.example.balancify.core.constant.PaginatedData
import com.example.balancify.domain.model.GroupMetadataModel
import com.example.balancify.domain.model.GroupModel
import com.google.firebase.firestore.DocumentSnapshot

interface GroupRemoteDataSource {
    suspend fun create(group: GroupModel, groupMetadata: GroupMetadataModel)
    suspend fun getGroupsWithUser(lastDoc: DocumentSnapshot?, id: String): PaginatedData<GroupModel>
    suspend fun getGroupById(id: String, userId: String): GroupModel
}