package com.example.balancify.domain.model

import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.serialization.Serializable

@IgnoreExtraProperties
@Serializable
data class ExpenseGroupModel(
    val id: String = "",
    val name: String = "",
)