package com.example.balancify.domain.use_case.search

import com.example.balancify.core.constant.PaginatedData
import com.example.balancify.core.constant.RepositoryResult
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
    ): RepositoryResult<PaginatedData<FoundItemModel>> {
        val friendResult = friendRepository.getFriends(lastDoc, search)

        if (friendResult !is RepositoryResult.Success) return friendResult as RepositoryResult.Error

        val enrichedResult = friendEnricher(friendResult.data.data)

        if (enrichedResult !is RepositoryResult.Success) return enrichedResult as RepositoryResult.Error

        val result = PaginatedData(
            data = enrichedResult.data.map {
                FoundItemModel(
                    id = it.userId,
                    imageUrl = it.user?.imageUrl ?: "",
                    name = it.name,
                    data = FoundItemData.Friend(it),
                )
            },
            canLoadMore = friendResult.data.canLoadMore,
            lastDoc = friendResult.data.lastDoc
        )

        return RepositoryResult.Success(result)
    }
}