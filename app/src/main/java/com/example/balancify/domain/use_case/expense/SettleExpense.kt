package com.example.balancify.domain.use_case.expense

import com.example.balancify.domain.repository.ExpenseRepository
import com.example.balancify.domain.repository.UserRepository

class SettleExpense(
    private val repository: ExpenseRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(
        id: String,
        amount: Double,
        settledAmount: Double,
        receiverName: String,
    ): Result<Unit> {
        val userResult = userRepository.getLocalUser()

        if (userResult.isFailure) return Result.failure(
            userResult.exceptionOrNull()!!
        )

        return repository.settleExpense(
            id,
            amount,
            settledAmount,
            userResult.getOrNull()!!,
            receiverName
        )
    }
}