package com.macrobytes.balancify.presentation.notification

import com.macrobytes.balancify.domain.model.NotificationModel
import com.macrobytes.balancify.domain.model.UserModel
import com.google.firebase.firestore.DocumentSnapshot

data class NotificationState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val canLoadMore: Boolean = false,
    val notifications: List<NotificationModel> = emptyList(),
    val lastDoc: DocumentSnapshot? = null,
    val localUser: UserModel? = null,
)