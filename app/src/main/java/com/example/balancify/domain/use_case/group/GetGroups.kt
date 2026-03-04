package com.example.balancify.domain.use_case.group

import com.example.balancify.domain.model.GroupModel
import com.example.balancify.domain.repository.GroupRepository
import com.example.balancify.domain.repository.UserRepository
import com.example.balancify.service.PaginatedData
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

        return repository.getGroupsWithUser(lastDoc, userResult.getOrNull()!!.id)
    }
}