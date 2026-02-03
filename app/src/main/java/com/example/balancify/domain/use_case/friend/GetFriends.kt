package com.example.balancify.domain.use_case.friend

import com.example.balancify.core.constant.RepositoryResult
import com.example.balancify.domain.model.PaginatedFriendsModel
import com.example.balancify.domain.model.UserModel
import com.example.balancify.domain.repository.FriendRepository
import com.example.balancify.domain.repository.UserRepository
import com.google.firebase.firestore.DocumentSnapshot

class GetFriends(
    private val repository: FriendRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(lastDoc: DocumentSnapshot?): RepositoryResult<PaginatedFriendsModel> {
        val result = repository.getFriends(lastDoc)

        if (result is RepositoryResult.Error) return result

        val userResult =
            userRepository.getUserByIds(
                (result as RepositoryResult.Success)
                    .data.friends.map {
                        it.userId
                    }
            )

        if (userResult is RepositoryResult.Error) return result

        return RepositoryResult.Success(
            data = PaginatedFriendsModel(
                friends = result.data.friends.map { friend ->
                    friend.copy(
                        user = (userResult as RepositoryResult.Success)
                            .data.find {
                                it.documentId == friend.userId
                            } ?: UserModel()
                    )
                },
                canLoadMore = result.data.canLoadMore
            )
        )

    }
}