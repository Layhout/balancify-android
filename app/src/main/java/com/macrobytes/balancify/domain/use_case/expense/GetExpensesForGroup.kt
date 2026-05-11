package com.macrobytes.balancify.domain.use_case.expense

import com.macrobytes.balancify.domain.model.ExpenseModel
import com.macrobytes.balancify.domain.repository.ExpenseRepository
import com.macrobytes.balancify.service.PaginatedData
import com.google.firebase.firestore.DocumentSnapshot

class GetExpensesForGroup(
    private val repository: ExpenseRepository,
) {
    suspend operator fun invoke(
        lastDoc: DocumentSnapshot?,
        groupId: String
    ): Result<PaginatedData<ExpenseModel>> = repository.getExpensesForGroup(lastDoc, groupId)
}