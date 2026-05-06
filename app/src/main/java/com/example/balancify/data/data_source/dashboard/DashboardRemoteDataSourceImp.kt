package com.example.balancify.data.data_source.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.balancify.core.constant.FirebaseCollectionName
import com.example.balancify.core.ext.format
import com.example.balancify.domain.model.DashboardModel
import com.example.balancify.domain.model.ExpenseModel
import com.example.balancify.service.AuthService
import com.example.balancify.service.DatabaseService
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import okhttp3.internal.toImmutableList
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DashboardRemoteDataSourceImp(
    private val db: DatabaseService,
    private val auth: AuthService,
) : DashboardRemoteDataSource {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getData(): DashboardModel {
        val result = db.getDocumentsWithQuery(
            FirebaseCollectionName.EXPENSES.value,
            queryBuilder = {
                it.whereArrayContains("memberIds", auth.userId)
                    .orderBy("createdAt", Query.Direction.DESCENDING)
            }
        )

        val expense = result.mapNotNull {
            it.toObject<ExpenseModel>()
        }

        var getBack = 0.0
        var owed = 0.0
        val spendingHistory = mutableListOf<Pair<String, Double>>()

        val now = LocalDate.now()
        (0..2).forEach {
            spendingHistory.add(
                now.minusMonths(
                    it.toLong()
                ).format(DateTimeFormatter.ofPattern("MMM")) to 0.0
            )
        }

        expense.forEach {
            if (it.paidBy.id == auth.userId) {
                getBack += it.amount - (it.member[auth.userId]?.amount ?: 0.0)
            } else {
                owed += (it.member[auth.userId]?.amount
                    ?: 0.0) - (it.member[auth.userId]?.settledAmount ?: 0.0)
            }

            if (it.createdAt == null) return@forEach

            val month: String = it.createdAt
                .format(
                    "MMM"
                )

            val amount = it.member[auth.userId]?.amount ?: 0.0
            val spendingHistoryMonthIndex =
                spendingHistory.indexOfFirst { sh ->
                    sh.first == month
                }

            if (spendingHistoryMonthIndex == -1) return@forEach

            spendingHistory[spendingHistoryMonthIndex] =
                spendingHistory[spendingHistoryMonthIndex].copy(
                    second = spendingHistory[spendingHistoryMonthIndex].second + amount
                )
        }

        return DashboardModel(
            getBack = getBack,
            owed = owed,
            expenses = expense,
            spendingHistory = spendingHistory.toImmutableList().reversed(),
        )
    }
}