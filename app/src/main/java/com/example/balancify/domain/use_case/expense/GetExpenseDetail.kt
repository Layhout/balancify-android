package com.example.balancify.domain.use_case.expense

import com.example.balancify.domain.model.ExpenseModel
import com.example.balancify.domain.repository.ExpenseRepository
import com.example.balancify.domain.repository.UserRepository

class GetExpenseDetail(
    private val repository: ExpenseRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(id: String): Result<ExpenseModel> {
        val userResult = userRepository.getLocalUser()

        if (userResult.isFailure) return Result.failure(
            userResult.exceptionOrNull()!!
        )

        return repository.getExpenseById(id, userResult.getOrNull()!!.id)
    }
}