package com.example.balancify.domain.service

import com.example.balancify.core.constant.RepositoryResult
import com.example.balancify.domain.model.FriendModel
import com.example.balancify.domain.model.UserModel
import com.example.balancify.domain.repository.UserRepository

class FriendEnricher(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(friends: List<FriendModel>): RepositoryResult<List<FriendModel>> {
        val userIds = friends.map { it.userId }

        val userResult = userRepository.getUserByIds(userIds)
        if (userResult !is RepositoryResult.Success) return userResult as RepositoryResult.Error

        val usersById = userResult.data.associateBy { it.documentId }

        return RepositoryResult.Success(
            friends.map { friend ->
                friend.copy(
                    user = usersById[friend.userId] ?: UserModel()
                )
            }
        )
    }
}
