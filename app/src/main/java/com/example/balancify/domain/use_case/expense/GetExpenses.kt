package com.example.balancify.domain.use_case.expense

import com.example.balancify.domain.model.ExpenseModel
import com.example.balancify.domain.repository.ExpenseRepository
import com.example.balancify.domain.repository.UserRepository
import com.example.balancify.service.PaginatedData
import com.google.firebase.firestore.DocumentSnapshot

class GetExpenses(
    private val repository: ExpenseRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(
        lastDoc: DocumentSnapshot?
    ): Result<PaginatedData<ExpenseModel>> {
        val userResult = userRepository.getLocalUser()

        if (userResult.isFailure) return Result.failure(
            userResult.exceptionOrNull()!!
        )

        return repository.getExpensesWithUser(lastDoc, userResult.getOrNull()!!.id)
    }
}