package com.macrobytes.balancify.data.repository

import com.macrobytes.balancify.data.data_source.expense.ExpenseRemoteDataSource
import com.macrobytes.balancify.domain.model.ExpenseMetadataModel
import com.macrobytes.balancify.domain.model.ExpenseModel
import com.macrobytes.balancify.domain.model.UserModel
import com.macrobytes.balancify.domain.repository.ExpenseRepository
import com.macrobytes.balancify.service.PaginatedData
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

    override suspend fun getExpensesForGroup(
        lastDoc: DocumentSnapshot?,
        groupId: String
    ): Result<PaginatedData<ExpenseModel>> {
        return Result.runCatching { remoteDataSource.getExpensesForGroup(lastDoc, groupId) }
    }

    override suspend fun deleteExpense(id: String): Result<Unit> {
        return Result.runCatching { remoteDataSource.deleteExpense(id) }
    }

    override suspend fun settleExpense(
        id: String,
        amount: Double,
        settledAmount: Double,
        localUser: UserModel,
        receiverName: String,
    ): Result<ExpenseModel> {
        return Result.runCatching {
            remoteDataSource.settleExpense(
                id,
                amount,
                settledAmount,
                localUser,
                receiverName
            )
        }
    }

    override suspend fun updateExpense(
        id: String,
        expense: ExpenseModel,
        expenseMetadata: ExpenseMetadataModel
    ): Result<Unit> {
        return Result.runCatching {
            remoteDataSource.updateExpense(id, expense, expenseMetadata)
        }
    }
}