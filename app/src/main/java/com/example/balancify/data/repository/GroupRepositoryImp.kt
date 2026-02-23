package com.example.balancify.data.repository

import com.example.balancify.core.constant.PaginatedData
import com.example.balancify.data.data_source.group.GroupRemoteDataSource
import com.example.balancify.domain.model.GroupMetadataModel
import com.example.balancify.domain.model.GroupModel
import com.example.balancify.domain.repository.GroupRepository

class GroupRepositoryImp(
    private val remoteDataSource: GroupRemoteDataSource
) : GroupRepository {
    override suspend fun createGroup(
        group: GroupModel,
        groupMetadata: GroupMetadataModel
    ): Result<Unit> {
        return try {
            remoteDataSource.create(group, groupMetadata)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getGroups(): Result<PaginatedData<GroupModel>> {
        TODO("Not yet implemented")
    }
}