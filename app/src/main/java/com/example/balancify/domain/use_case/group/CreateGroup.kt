package com.example.balancify.domain.use_case.group

import com.example.balancify.core.ext.getTrigram
import com.example.balancify.domain.model.GroupMetadataModel
import com.example.balancify.domain.model.GroupModel
import com.example.balancify.domain.model.UserModel
import com.example.balancify.domain.repository.GroupRepository
import com.example.balancify.domain.repository.UserRepository
import java.util.UUID

class CreateGroup(
    private val repository: GroupRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(
        name: String,
        description: String,
        members: List<UserModel>,
    ): Result<Unit> {
        val userResult = userRepository.getLocalUser()

        if (userResult.isFailure) return Result.failure(
            userResult.exceptionOrNull()!!
        )

        val groupMembers: List<UserModel> = members + userResult.getOrNull()!!

        val group = GroupModel(
            name = name,
            description = description,
            id = UUID.randomUUID().toString(),
            members = groupMembers,
            memberIds = groupMembers.map { it.id },
            createdBy = userResult.getOrNull()!!.id,
        )

        val groupMetaData = GroupMetadataModel(
            groupId = group.id,
            nameTrigrams = group.name.getTrigram(),
            membersFlag = groupMembers.associate { it.id to true }
        )

        return repository.createGroup(group, groupMetaData)
    }
}