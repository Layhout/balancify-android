package com.example.balancify.presentation.notification

import com.example.balancify.domain.model.NotificationModel
import com.example.balancify.domain.model.UserModel
import com.google.firebase.firestore.DocumentSnapshot

data class NotificationState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val canLoadMore: Boolean = false,
    val notifications: List<NotificationModel> = emptyList(),
    val lastDoc: DocumentSnapshot? = null,
    val localUser: UserModel? = null,
)