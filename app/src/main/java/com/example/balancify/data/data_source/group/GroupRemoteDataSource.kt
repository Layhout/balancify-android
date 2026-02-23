package com.example.balancify.data.data_source.group

import com.example.balancify.core.constant.PaginatedData
import com.example.balancify.domain.model.GroupMetadataModel
import com.example.balancify.domain.model.GroupModel

interface GroupRemoteDataSource {
    suspend fun create(group: GroupModel, groupMetadata: GroupMetadataModel)
    suspend fun getGroups(): PaginatedData<GroupModel>
}