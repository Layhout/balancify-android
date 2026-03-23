package com.example.balancify.domain.model

import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.serialization.Serializable

@IgnoreExtraProperties
@Serializable
data class ExpenseMetadataModel(
    val expenseId: String = "",
    val nameTrigrams: List<String> = emptyList(),
    val membersFlag: Map<String, Boolean> = emptyMap(),
)