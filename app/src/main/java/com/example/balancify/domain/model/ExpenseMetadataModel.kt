package com.example.balancify.domain.model

data class ExpenseMetadataModel(
    val expenseId: String = "",
    val nameTrigrams: List<String> = emptyList(),
    val membersFlag: Map<String, Boolean> = emptyMap(),
)