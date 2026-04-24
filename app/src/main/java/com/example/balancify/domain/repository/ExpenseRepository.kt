package com.example.balancify.domain.repository

import com.example.balancify.domain.model.ExpenseMetadataModel
import com.example.balancify.domain.model.ExpenseModel
import com.example.balancify.domain.model.UserModel
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

    suspend fun getExpensesForGroup(
        lastDoc: DocumentSnapshot?,
        groupId: String,
    ): Result<PaginatedData<ExpenseModel>>

    suspend fun deleteExpense(id: String): Result<Unit>
    suspend fun settleExpense(
        id: String,
        amount: Double,
        settledAmount: Double,
        localUser: UserModel,
        receiverName: String,
    ): Result<ExpenseModel>

    suspend fun updateExpense(
        id: String,
        expense: ExpenseModel,
        expenseMetadata: ExpenseMetadataModel,
    ): Result<Unit>
}