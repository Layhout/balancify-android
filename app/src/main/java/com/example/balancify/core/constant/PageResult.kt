package com.example.balancify.core.constant

import com.google.firebase.firestore.QuerySnapshot

data class PageResult(
    val snapshot: QuerySnapshot,
    val canLoadMore: Boolean,
)
