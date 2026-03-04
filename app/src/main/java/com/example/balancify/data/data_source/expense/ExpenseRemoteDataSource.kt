package com.example.balancify.data.data_source.expense

import com.example.balancify.domain.model.ExpenseMetadataModel
import com.example.balancify.domain.model.ExpenseModel
import com.example.balancify.service.PaginatedData
import com.google.firebase.firestore.DocumentSnapshot

interface ExpenseRemoteDataSource {
    suspend fun createExpense(
        expense: ExpenseModel,
        expenseMetadata: ExpenseMetadataModel,
    )

    suspend fun getExpensesWithUser(
        lastDoc: DocumentSnapshot?,
        id: String,
    ): PaginatedData<ExpenseModel>

    suspend fun getExpenseById(id: String, userId: String): ExpenseModel
}