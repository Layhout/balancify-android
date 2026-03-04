package com.example.balancify.domain.use_case.group

import com.example.balancify.domain.model.GroupMetadataModel
import com.example.balancify.domain.model.GroupModel
import com.example.balancify.domain.model.UserModel
import com.example.balancify.domain.repository.GroupRepository
import com.example.balancify.domain.repository.UserRepository

class LeaveGroup(
    private val repository: GroupRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(id: String, members: List<UserModel>): Result<Unit> {
        val userResult = userRepository.getLocalUser()

        if (userResult.isFailure) return Result.failure(
            userResult.exceptionOrNull()!!
        )

        val newMembers = members.filter { it.id != userResult.getOrNull()!!.id }

        val group = GroupModel(
            members = newMembers,
            memberIds = newMembers.map { it.id },
        )

        val groupMetaData = GroupMetadataModel(
            membersFlag = newMembers.associate { it.id to true }
        )

        return repository.leaveGroup(id, group, groupMetaData)
    }
}