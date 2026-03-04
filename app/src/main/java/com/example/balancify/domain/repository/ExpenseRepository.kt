package com.example.balancify.domain.repository

import com.example.balancify.domain.model.ExpenseMetadataModel
import com.example.balancify.domain.model.ExpenseModel
import com.example.balancify.service.PaginatedData
import com.google.firebase.firestore.DocumentSnapshot

interface ExpenseRepository {
    suspend fun createExpense(
        expense: ExpenseModel,
        expenseMetadata: ExpenseMetadataModel,
    ): Result<Unit>

    suspend fun getExpensesWithUser(
        lastDoc: DocumentSnapshot?,
        id: String,
    ): Result<PaginatedData<ExpenseModel>>

    suspend fun getExpenseById(id: String, userId: String): Result<ExpenseModel>
}