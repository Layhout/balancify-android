package com.macrobytes.balancify.domain.use_case.group

import com.macrobytes.balancify.domain.model.GroupModel
import com.macrobytes.balancify.domain.repository.GroupRepository
import com.macrobytes.balancify.domain.repository.UserRepository
import com.macrobytes.balancify.service.PaginatedData
import com.google.firebase.firestore.DocumentSnapshot

class GetGroups(
    private val repository: GroupRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(lastDoc: DocumentSnapshot?): Result<PaginatedData<GroupModel>> {
        val userResult = userRepository.getLocalUser()

        if (userResult.isFailure) return Result.failure(
            userResult.exceptionOrNull()!!
        )

        return repository.getGroupsWithUser(lastDoc, userResult.getOrNull()!!.id, null)
    }
}