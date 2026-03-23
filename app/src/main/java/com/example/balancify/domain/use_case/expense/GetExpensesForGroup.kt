package com.example.balancify.domain.use_case.expense

import com.example.balancify.domain.model.ExpenseModel
import com.example.balancify.domain.repository.ExpenseRepository
import com.example.balancify.service.PaginatedData
import com.google.firebase.firestore.DocumentSnapshot

class GetExpensesForGroup(
    private val repository: ExpenseRepository,
) {
    suspend operator fun invoke(
        lastDoc: DocumentSnapshot?,
        groupId: String
    ): Result<PaginatedData<ExpenseModel>> = repository.getExpensesForGroup(lastDoc, groupId)
}