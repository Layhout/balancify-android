package com.example.balancify.domain.use_case.group

import com.example.balancify.core.ext.getTrigram
import com.example.balancify.domain.model.GroupMetadataModel
import com.example.balancify.domain.model.GroupModel
import com.example.balancify.domain.model.UserModel
import com.example.balancify.domain.repository.GroupRepository
import com.example.balancify.domain.repository.UserRepository

class UpdateGroup(
    private val repository: GroupRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(
        id: String, name: String,
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
            members = groupMembers,
            memberIds = groupMembers.map { it.id },
        )

        val groupMetaData = GroupMetadataModel(
            nameTrigrams = group.name.getTrigram(),
            membersFlag = groupMembers.associate { it.id to true }
        )

        return repository.updateGroup(id, group, groupMetaData)
    }

}