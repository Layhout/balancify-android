package com.macrobytes.balancify.domain.service

import com.macrobytes.balancify.domain.model.FriendModel
import com.macrobytes.balancify.domain.model.UserModel
import com.macrobytes.balancify.domain.repository.UserRepository

class FriendEnricher(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(friends: List<FriendModel>): Result<List<FriendModel>> {
        val userIds = friends.map { it.userId }

        val userResult = userRepository.getUserByIds(userIds)
        if (userResult.isFailure) return Result.failure(
            userResult.exceptionOrNull() ?: Exception(
                "Unknown error at FriendEnricher use case"
            )
        )

        val usersById = userResult.getOrNull()?.associateBy { it.id } ?: emptyMap()

        return Result.success(
            friends.map { friend ->
                friend.copy(
                    user = usersById[friend.userId] ?: UserModel()
                )
            }
        )
    }
}
