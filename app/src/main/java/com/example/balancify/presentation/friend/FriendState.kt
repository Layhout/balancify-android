package com.example.balancify.presentation.friend

import com.example.balancify.domain.model.FriendModel
import com.google.firebase.firestore.DocumentSnapshot

data class FriendState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val canLoadMore: Boolean = false,
    val friends: List<FriendModel> = emptyList(),
    val lastDoc: DocumentSnapshot? = null,
)
