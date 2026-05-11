package com.macrobytes.balancify.domain.use_case.search

import com.macrobytes.balancify.domain.model.FoundItemData
import com.macrobytes.balancify.domain.model.FoundItemModel
import com.macrobytes.balancify.domain.repository.GroupRepository
import com.macrobytes.balancify.domain.repository.UserRepository
import com.macrobytes.balancify.service.PaginatedData
import com.google.firebase.firestore.DocumentSnapshot

class FindGroups(
    private val groupRepository: GroupRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(
        lastDoc: DocumentSnapshot?,
        search: String?,
    ): Result<PaginatedData<FoundItemModel>> {
        if (search.isNullOrBlank()) return Result.failure(Exception("Search cannot be empty"))

        if (search.length < 3) return Result.failure(Exception("Search must be at least 3 characters"))

        val userResult = userRepository.getLocalUser()

        if (userResult.isFailure) return Result.failure(
            userResult.exceptionOrNull()!!
        )

        val groupResult =
            groupRepository.getGroupsWithUser(lastDoc, userResult.getOrNull()!!.id, search)

        if (groupResult.isFailure) return Result.failure(
            groupResult.exceptionOrNull()!!
        )

        val result = PaginatedData(
            data = groupResult.getOrNull()?.data?.map {
                FoundItemModel(
                    id = it.id,
                    imageUrl = "",
                    name = it.name,
                    data = FoundItemData.Group(it),
                )
            } ?: emptyList(),
            canLoadMore = groupResult.getOrNull()?.canLoadMore ?: false,
            lastDoc = groupResult.getOrNull()?.lastDoc
        )

        return Result.success(result)
    }
}