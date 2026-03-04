package com.example.balancify.domain.use_case.group

import com.example.balancify.domain.model.GroupModel
import com.example.balancify.domain.repository.GroupRepository
import com.example.balancify.domain.repository.UserRepository

class GetGroupDetail(
    private val repository: GroupRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(id: String): Result<GroupModel> {
        val userResult = userRepository.getLocalUser()

        if (userResult.isFailure) return Result.failure(
            userResult.exceptionOrNull()!!
        )

        return repository.getGroupById(id, userResult.getOrNull()!!.id)
    }
}