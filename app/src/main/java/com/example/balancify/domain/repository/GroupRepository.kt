package com.example.balancify.domain.repository

import com.example.balancify.core.constant.PaginatedData
import com.example.balancify.domain.model.GroupMetadataModel
import com.example.balancify.domain.model.GroupModel
import com.example.balancify.domain.model.UserModel
import com.google.firebase.firestore.DocumentSnapshot

interface GroupRepository {
    suspend fun createGroup(
        group: GroupModel,
        groupMetadata: GroupMetadataModel
    ): Result<Unit>

    suspend fun getGroupsWithUser(
        lastDoc: DocumentSnapshot?,
        id: String
    ): Result<PaginatedData<GroupModel>>

    suspend fun getGroupById(id: String, userId: String): Result<GroupModel>
    suspend fun leaveGroup(id: String, user: UserModel): Result<Unit>
    suspend fun deleteGroup(id: String): Result<Unit>
}