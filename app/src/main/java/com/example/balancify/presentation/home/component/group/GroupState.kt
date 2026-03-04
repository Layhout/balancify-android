package com.example.balancify.presentation.home.component.group

import com.example.balancify.domain.model.GroupModel
import com.google.firebase.firestore.DocumentSnapshot

data class GroupState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val canLoadMore: Boolean = true,
    val groups: List<GroupModel> = emptyList(),
    val lastDoc: DocumentSnapshot? = null,
)
