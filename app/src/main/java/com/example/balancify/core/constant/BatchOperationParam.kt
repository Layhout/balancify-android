package com.example.balancify.core.constant

data class BatchSetItem(
    val collection: String,
    val id: String,
    val data: Any,
    val merge: Boolean = true
)

data class BatchUpdateItem(
    val collection: String,
    val id: String,
    val fields: Map<String, Any?>
)

data class BatchDeleteItem(
    val collection: String,
    val id: String
)