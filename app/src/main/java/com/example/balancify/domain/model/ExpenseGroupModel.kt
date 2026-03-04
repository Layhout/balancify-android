package com.example.balancify.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ExpenseGroupModel(
    val id: String = "",
    val name: String = "",
)