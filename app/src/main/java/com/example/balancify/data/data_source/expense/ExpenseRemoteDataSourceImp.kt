package com.example.balancify.data.data_source.expense

import com.example.balancify.core.constant.FirebaseCollectionName
import com.example.balancify.core.constant.ITEMS_LIMIT
import com.example.balancify.domain.model.ExpenseMetadataModel
import com.example.balancify.domain.model.ExpenseModel
import com.example.balancify.service.BatchSetItem
import com.example.balancify.service.DatabaseService
import com.example.balancify.service.PaginatedData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath.documentId
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject

class ExpenseRemoteDataSourceImp(
    private val db: DatabaseService,
) : ExpenseRemoteDataSource {
    private val collectionName: String = FirebaseCollectionName.EXPENSES.value
    private val metaDataCollectionName: String = FirebaseCollectionName.EXPENSE_METADATA.value

    override suspend fun createExpense(
        expense: ExpenseModel,
        expenseMetadata: ExpenseMetadataModel,
    ) {
        db.batchSet(
            listOf(
                BatchSetItem(
                    collection = collectionName,
                    id = expense.id,
                    data = expense,
                ),
                BatchSetItem(
                    collection = metaDataCollectionName,
                    id = expense.id,
                    data = expenseMetadata
                ),
            )
        )
    }

    override suspend fun getExpensesWithUser(
        lastDoc: DocumentSnapshot?,
        id: String
    ): PaginatedData<ExpenseModel> {
        val result = db.getPage(collectionName, ITEMS_LIMIT, lastDoc, queryBuilder = {
            it.whereArrayContains("memberIds", id)
                .orderBy("createdAt", Query.Direction.DESCENDING)
        })

        val expense = result.snapshot.documents.mapNotNull {
            it.toObject<ExpenseModel>()
        }

        val canLoadMore = result.canLoadMore

        return PaginatedData(
            data = expense,
            canLoadMore = canLoadMore,
            lastDoc = result.snapshot.documents.lastOrNull()
        )
    }

    override suspend fun getExpenseById(
        id: String,
        userId: String
    ): ExpenseModel {
        val result = db.getPage(collectionName, 1, null, queryBuilder = {
            it.whereEqualTo(documentId(), id)
                .whereArrayContains("memberIds", userId)
        })

        val expense = result.snapshot.documents.firstOrNull()?.toObject<ExpenseModel>()

        return expense ?: throw Exception("Group not found")
    }
}