package com.example.balancify.domain.model

import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.serialization.Serializable

@IgnoreExtraProperties
@Serializable
data class DashboardModel(
    val getBack: Double,
    val owed: Double,
    val expenses: List<ExpenseModel>,
    val spendingHistory: List<Pair<String, Double>>
)
