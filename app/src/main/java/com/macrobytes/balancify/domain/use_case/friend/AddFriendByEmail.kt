package com.macrobytes.balancify.domain.use_case.friend

import com.macrobytes.balancify.core.ext.getTrigram
import com.macrobytes.balancify.domain.model.FriendModel
import com.macrobytes.balancify.domain.model.FriendStatus
import com.macrobytes.balancify.domain.model.NotificationModel
import com.macrobytes.balancify.domain.model.NotificationType
import com.macrobytes.balancify.domain.repository.FriendRepository
import com.macrobytes.balancify.domain.repository.NotificationRepository
import com.macrobytes.balancify.domain.repository.UserRepository
import java.util.UUID

class AddFriendByEmail(
    private val repository: FriendRepository,
    private val userRepository: UserRepository,
    private val notificationRepository: NotificationRepository,
) {
    suspend operator fun invoke(email: String): Result<FriendModel> {
        val userResult = userRepository.getUserByEmail(email)

        if (userResult.isFailure) return Result.failure(
            userResult.exceptionOrNull()!!
        )

        val foundUser = userResult.getOrNull()
            ?: return Result.failure(Exception("USER404"))

        val friendResult = repository.getFriend(foundUser.id)

        if (friendResult.isFailure) return Result.failure(
            friendResult.exceptionOrNull()!!
        )

        val foundFriend = friendResult.getOrNull()

        if (foundFriend != null && foundFriend.status == FriendStatus.ACCEPTED)
            return Result.failure(
                Exception("Friend already exists")
            )

        val localUserResult = userRepository.getLocalUser()

        if (localUserResult.isFailure) return Result.failure(
            localUserResult.exceptionOrNull()!!
        )

        val localUser = localUserResult.getOrNull()
            ?: return Result.failure(Exception("Error local user"))

        val friend = FriendModel(
            userId = foundUser.id,
            name = foundUser.name,
            status = FriendStatus.PENDING,
            nameTrigrams = foundUser.name.getTrigram(),
        )

        val youAsFriend = FriendModel(
            userId = localUser.id,
            name = localUser.name,
            status = FriendStatus.REQUESTING,
            nameTrigrams = localUser.name.getTrigram(),
        )

        val result = repository.addFriend(friend, youAsFriend)

        if (result.isFailure) return Result.failure(
            result.exceptionOrNull()!!
        )

        notificationRepository.createNotification(
            NotificationModel(
                id = UUID.randomUUID().toString(),
                title = "New Friend Request",
                description = "${userResult.getOrNull()!!.name} sent you a firend request.",
                link = "/app/friends",
                type = NotificationType.FRIEND_REQUEST,
                userReadFlag = mapOf(
                    foundFriend!!.userId to false
                ),
                ownerIds = listOf(foundFriend.userId),
            )
        )

        return Result.success(friend.copy(user = foundUser))
    }
}