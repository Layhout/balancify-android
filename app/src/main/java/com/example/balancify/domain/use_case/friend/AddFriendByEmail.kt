package com.example.balancify.domain.use_case.friend

import com.example.balancify.core.constant.FriendStatus
import com.example.balancify.core.ext.getTrigram
import com.example.balancify.domain.model.FriendModel
import com.example.balancify.domain.repository.FriendRepository
import com.example.balancify.domain.repository.UserRepository

class AddFriendByEmail(
    private val repository: FriendRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(email: String): Result<FriendModel> {
        val userResult = userRepository.getUserByEmail(email)

        if (userResult.isFailure) return Result.failure(
            userResult.exceptionOrNull() ?: Exception(
                "Unknown error AddFriendByEmail use case"
            )
        )

        val foundUser = userResult.getOrNull()
            ?: return Result.failure(Exception("USER404"))

        val friendResult = repository.getFriend(foundUser.id)

        if (friendResult.isFailure) return Result.failure(
            friendResult.exceptionOrNull() ?: Exception(
                "Unknown error AddFriendByEmail use case"
            )
        )

        val foundFriend = friendResult.getOrNull()

        if (foundFriend != null && foundFriend.status == FriendStatus.ACCEPTED)
            return Result.failure(
                Exception("Friend already exists")
            )

        val localUserResult = userRepository.getLocalUser()

        if (localUserResult.isFailure) return Result.failure(
            localUserResult.exceptionOrNull() ?: Exception(
                "Unknown error AddFriendByEmail use case"
            )
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
            result.exceptionOrNull() ?: Exception(
                "Unknown error AddFriendByEmail use case"
            )
        )

        return Result.success(friend.copy(user = foundUser))
    }
}