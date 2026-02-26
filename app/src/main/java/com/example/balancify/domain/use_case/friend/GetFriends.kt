package com.example.balancify.domain.use_case.friend

import com.example.balancify.core.constant.PaginatedData
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
    ): Result<PaginatedData<FriendModel>> {
        val result = repository.getFriends(lastDoc)

        if (result.isFailure) return result

        val enrichedResult = friendEnricher(result.getOrNull()?.data ?: emptyList())

        if (enrichedResult.isFailure) return Result.failure(
            enrichedResult.exceptionOrNull()!!
        )

        return Result.success(
            result.getOrNull()?.copy(
                data = enrichedResult.getOrNull()
                    ?: emptyList()
            ) ?: PaginatedData()
        )
    }
}