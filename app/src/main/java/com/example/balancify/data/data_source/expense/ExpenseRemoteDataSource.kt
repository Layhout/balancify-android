package com.example.balancify.data.data_source.expense

import com.example.balancify.domain.model.ExpenseMetadataModel
import com.example.balancify.domain.model.ExpenseModel
import com.example.balancify.domain.model.UserModel
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

    suspend fun getExpensesForGroup(
        lastDoc: DocumentSnapshot?,
        groupId: String,
    ): PaginatedData<ExpenseModel>

    suspend fun deleteExpense(id: String)

    suspend fun settleExpense(
        id: String,
        amount: Double,
        settledAmount: Double,
        localUser: UserModel,
        receiverName: String,
    ): ExpenseModel

    suspend fun updateExpense(
        id: String,
        expense: ExpenseModel,
        expenseMetadata: ExpenseMetadataModel,
    )
}