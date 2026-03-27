package com.example.balancify.data.data_source.expense

import com.example.balancify.core.constant.FirebaseCollectionName
import com.example.balancify.core.constant.ITEMS_LIMIT
import com.example.balancify.core.ext.getCurrencyFormatted
import com.example.balancify.domain.model.ExpenseMemberModel
import com.example.balancify.domain.model.ExpenseMetadataModel
import com.example.balancify.domain.model.ExpenseModel
import com.example.balancify.domain.model.TimelineModel
import com.example.balancify.domain.model.UserModel
import com.example.balancify.service.BatchDeleteItem
import com.example.balancify.service.BatchSetItem
import com.example.balancify.service.DatabaseService
import com.example.balancify.service.PaginatedData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath.documentId
import com.google.firebase.firestore.FieldValue.arrayUnion
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
        val result = db.getDataWithQuery(collectionName, queryBuilder = {
            it.whereEqualTo(documentId(), id)
                .whereArrayContains("memberIds", userId)
        })

        val expense = result.documents.firstOrNull()?.toObject<ExpenseModel>()

        val timelines = expense?.timelines
        val sortedTimelines = timelines?.sortedByDescending { it.createdAt }

        return expense?.copy(
            timelines = sortedTimelines ?: emptyList()
        ) ?: throw Exception("Group not found")
    }

    override suspend fun getExpensesForGroup(
        lastDoc: DocumentSnapshot?,
        groupId: String
    ): PaginatedData<ExpenseModel> {
        val result = db.getPage(collectionName, ITEMS_LIMIT, lastDoc, queryBuilder = {
            it.whereEqualTo("group.id", groupId)
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

    override suspend fun deleteExpense(id: String) {
        db.batchDelete(
            listOf(
                BatchDeleteItem(
                    collection = collectionName,
                    id = id
                ),
                BatchDeleteItem(
                    collection = metaDataCollectionName,
                    id = id
                )
            )
        )
    }

    override suspend fun settleExpense(
        id: String,
        amount: Double,
        settledAmount: Double,
        localUser: UserModel,
        receiverName: String
    ): ExpenseModel {
        val createdAt = System.currentTimeMillis()
        val events =
            "${localUser.name} pays $receiverName with amount ${settledAmount.getCurrencyFormatted()}"

        db.updateData(
            collection = collectionName,
            id = id,
            data = mapOf(
                "member.${localUser.id}.settledAmount" to amount,
                "timelines" to arrayUnion(
                    mapOf(
                        "createdAt" to createdAt,
                        "createdBy" to localUser,
                        "events" to events
                    )
                )
            )
        )

        return ExpenseModel(
            member = mapOf(
                localUser.id to ExpenseMemberModel(
                    settledAmount = amount
                ),
            ),
            timelines = listOf(
                TimelineModel(
                    createdAt = createdAt,
                    createdBy = localUser,
                    events = events
                )
            )
        )
    }
}