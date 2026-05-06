package com.example.balancify.domain.use_case.group

import com.example.balancify.core.ext.getTrigram
import com.example.balancify.domain.model.GroupMetadataModel
import com.example.balancify.domain.model.GroupModel
import com.example.balancify.domain.model.NotificationModel
import com.example.balancify.domain.model.NotificationType
import com.example.balancify.domain.model.UserModel
import com.example.balancify.domain.repository.GroupRepository
import com.example.balancify.domain.repository.NotificationRepository
import com.example.balancify.domain.repository.UserRepository
import java.util.UUID

class CreateGroup(
    private val repository: GroupRepository,
    private val userRepository: UserRepository,
    private val notificationRepository: NotificationRepository,
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

        notificationRepository.createNotification(
            NotificationModel(
                id = UUID.randomUUID().toString(),
                title = "New Group",
                description = "${userResult.getOrNull()!!.name} added you to a group.",
                link = "/app/groups/${group.id}",
                type = NotificationType.EXPENSE,
                userReadFlag = members.associate { it.id to false },
                ownerIds = members.map { it.id },
            )
        )

        return repository.createGroup(group, groupMetaData)
    }
}