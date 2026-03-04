package com.example.balancify.service

import com.google.firebase.firestore.DocumentSnapshot

data class PaginatedData<T>(
    val data: List<T> = emptyList(),
    val canLoadMore: Boolean = false,
    val lastDoc: DocumentSnapshot? = null,
)