package com.example.balancify.domain.use_case.search

import com.example.balancify.core.constant.PaginatedData
import com.example.balancify.domain.model.FoundItemData
import com.example.balancify.domain.model.FoundItemModel
import com.example.balancify.domain.repository.FriendRepository
import com.example.balancify.domain.service.FriendEnricher
import com.google.firebase.firestore.DocumentSnapshot

class FindFriends(
    private val friendRepository: FriendRepository,
    private val friendEnricher: FriendEnricher,
) {
    suspend operator fun invoke(
        lastDoc: DocumentSnapshot?,
        search: String?,
    ): Result<PaginatedData<FoundItemModel>> {
        if (search.isNullOrBlank()) return Result.failure(Exception("Search cannot be empty"))

        if (search.length < 3) return Result.failure(Exception("Search must be at least 3 characters"))

        val friendResult = friendRepository.getFriends(lastDoc, search)

        if (friendResult.isFailure) return Result.failure(
            friendResult.exceptionOrNull() ?: Exception(
                "Unknown error FindFriends use case"
            )
        )

        val enrichedResult = friendEnricher(friendResult.getOrNull()?.data ?: emptyList())

        if (enrichedResult.isFailure) return Result.failure(
            enrichedResult.exceptionOrNull() ?: Exception(
                "Unknown error FindFriends use case"
            )
        )

        val result = PaginatedData(
            data = enrichedResult.getOrNull()?.map {
                FoundItemModel(
                    id = it.userId,
                    imageUrl = it.user?.imageUrl ?: "",
                    name = it.name,
                    data = FoundItemData.Friend(it),
                )
            } ?: emptyList(),
            canLoadMore = friendResult.getOrNull()?.canLoadMore ?: false,
            lastDoc = friendResult.getOrNull()?.lastDoc
        )

        return Result.success(result)
    }
}