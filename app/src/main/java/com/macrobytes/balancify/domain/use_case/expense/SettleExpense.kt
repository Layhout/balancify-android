package com.macrobytes.balancify.domain.use_case.expense

import com.macrobytes.balancify.domain.model.ExpenseModel
import com.macrobytes.balancify.domain.repository.ExpenseRepository
import com.macrobytes.balancify.domain.repository.UserRepository

class SettleExpense(
    private val repository: ExpenseRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(
        id: String,
        amount: Double,
        settledAmount: Double,
        receiverName: String,
    ): Result<ExpenseModel> {
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