package com.example.balancify.domain.use_case.friend

import com.example.balancify.core.constant.PaginatedData
import com.example.balancify.core.constant.RepositoryResult
import com.example.balancify.domain.model.FriendModel
import com.example.balancify.domain.repository.FriendRepository
import com.example.balancify.domain.service.FriendEnricher
import com.google.firebase.firestore.DocumentSnapshot

class GetFriends(
    private val repository: FriendRepository,
    private val friendEnricher: FriendEnricher,
) {
    suspend operator fun invoke(
        lastDoc: DocumentSnapshot?,
    ): RepositoryResult<PaginatedData<FriendModel>> {
        val result = repository.getFriends(lastDoc)

        if (result !is RepositoryResult.Success) return result

        val enrichedResult = friendEnricher(result.data.data)

        if (enrichedResult !is RepositoryResult.Success) return enrichedResult as RepositoryResult.Error

        return RepositoryResult.Success(
            result.data.copy(data = enrichedResult.data)
        )
    }
}