package com.example.balancify.domain.repository

import com.example.balancify.core.constant.PaginatedData
import com.example.balancify.domain.model.GroupMetadataModel
import com.example.balancify.domain.model.GroupModel

interface GroupRepository {
    suspend fun createGroup(group: GroupModel, groupMetadata: GroupMetadataModel): Result<Unit>
    suspend fun getGroups(): Result<PaginatedData<GroupModel>>
}