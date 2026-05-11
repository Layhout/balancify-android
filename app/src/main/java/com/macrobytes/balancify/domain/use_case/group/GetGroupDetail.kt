package com.macrobytes.balancify.domain.use_case.group

import com.macrobytes.balancify.domain.model.GroupModel
import com.macrobytes.balancify.domain.repository.GroupRepository
import com.macrobytes.balancify.domain.repository.UserRepository

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