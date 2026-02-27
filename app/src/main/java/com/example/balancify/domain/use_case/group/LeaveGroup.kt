package com.example.balancify.domain.use_case.group

import com.example.balancify.domain.repository.GroupRepository
import com.example.balancify.domain.repository.UserRepository

class LeaveGroup(
    private val repository: GroupRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        val userResult = userRepository.getLocalUser()

        if (userResult.isFailure) return Result.failure(
            userResult.exceptionOrNull()!!
        )

        return repository.leaveGroup(id, userResult.getOrNull()!!)
    }
}