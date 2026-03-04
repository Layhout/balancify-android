package com.example.balancify.service

import com.google.firebase.firestore.QuerySnapshot

data class PageResult(
    val snapshot: QuerySnapshot,
    val canLoadMore: Boolean,
)