package com.example.balancify.data.repository

import com.example.balancify.data.data_source.group.GroupRemoteDataSource
import com.example.balancify.domain.model.GroupMetadataModel
import com.example.balancify.domain.model.GroupModel
import com.example.balancify.domain.repository.GroupRepository
import com.example.balancify.service.PaginatedData
import com.google.firebase.firestore.DocumentSnapshot

class GroupRepositoryImp(
    private val remoteDataSource: GroupRemoteDataSource
) : GroupRepository {
    override suspend fun createGroup(
        group: GroupModel,
        groupMetadata: GroupMetadataModel
    ): Result<Unit> {
        return Result.runCatching {
            remoteDataSource.createGroup(group, groupMetadata)
        }
    }

    override suspend fun getGroupsWithUser(
        lastDoc: DocumentSnapshot?,
        id: String
    ): Result<PaginatedData<GroupModel>> {
        return Result.runCatching {
            remoteDataSource.getGroupsWithUser(lastDoc, id)
        }
    }

    override suspend fun getGroupById(
        id: String,
        userId: String
    ): Result<GroupModel> {
        return Result.runCatching {
            remoteDataSource.getGroupById(id, userId)
        }
    }

    override suspend fun leaveGroup(
        id: String,
        group: GroupModel,
        groupMetadata: GroupMetadataModel,
    ): Result<Unit> {
        return Result.runCatching {
            remoteDataSource.leaveGroup(id, group, groupMetadata)
        }
    }

    override suspend fun deleteGroup(id: String): Result<Unit> {
        return Result.runCatching {
            remoteDataSource.deleteGroup(id)
        }
    }

    override suspend fun updateGroup(
        id: String,
        group: GroupModel,
        groupMetadata: GroupMetadataModel,
    ): Result<Unit> {
        return Result.runCatching {
            remoteDataSource.updateGroup(id, group, groupMetadata)
        }
    }
}