package com.example.balancify.domain.use_case.friend

import com.example.balancify.core.constant.FriendStatus
import com.example.balancify.core.constant.RepositoryResult
import com.example.balancify.core.ext.getTrigram
import com.example.balancify.domain.model.FriendModel
import com.example.balancify.domain.repository.FriendRepository
import com.example.balancify.domain.repository.UserRepository

class AddFriendByEmail(
    private val repository: FriendRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(email: String): RepositoryResult<FriendModel> {
        val userResult = userRepository.getUserByEmail(email)

        if (userResult is RepositoryResult.Error) return userResult

        val foundUser = (userResult as RepositoryResult.Success).data
            ?: return RepositoryResult.Error(Exception("USER404"))

        val friendResult = repository.getFriend(foundUser.id)

        if (friendResult is RepositoryResult.Error) return friendResult

        val foundFriend = (friendResult as RepositoryResult.Success).data

        if (foundFriend != null && foundFriend.status == FriendStatus.ACCEPTED)
            return RepositoryResult.Error(
                Exception("Friend already exists")
            )

        val localUserResult = userRepository.getLocalUser()

        if (localUserResult is RepositoryResult.Error) return localUserResult

        val localUser = (localUserResult as RepositoryResult.Success).data
            ?: return RepositoryResult.Error(Exception("Error local user"))

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

        if (result is RepositoryResult.Error) return result

        return RepositoryResult.Success(friend.copy(user = foundUser))
    }
}