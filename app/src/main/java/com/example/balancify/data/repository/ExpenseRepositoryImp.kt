package com.example.balancify.data.repository

import com.example.balancify.data.data_source.expense.ExpenseRemoteDataSource
import com.example.balancify.domain.model.ExpenseMetadataModel
import com.example.balancify.domain.model.ExpenseModel
import com.example.balancify.domain.repository.ExpenseRepository
import com.example.balancify.service.PaginatedData
import com.google.firebase.firestore.DocumentSnapshot

class ExpenseRepositoryImp(
    private val remoteDataSource: ExpenseRemoteDataSource
) : ExpenseRepository {
    override suspend fun createExpense(
        expense: ExpenseModel,
        expenseMetadata: ExpenseMetadataModel
    ): Result<Unit> {
        return Result.runCatching { remoteDataSource.createExpense(expense, expenseMetadata) }
    }

    override suspend fun getExpensesWithUser(
        lastDoc: DocumentSnapshot?,
        id: String
    ): Result<PaginatedData<ExpenseModel>> {
        return Result.runCatching { remoteDataSource.getExpensesWithUser(lastDoc, id) }
    }

    override suspend fun getExpenseById(
        id: String,
        userId: String
    ): Result<ExpenseModel> {
        return Result.runCatching { remoteDataSource.getExpenseById(id, userId) }
    }
}