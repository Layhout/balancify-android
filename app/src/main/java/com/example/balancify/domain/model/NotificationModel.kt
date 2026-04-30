package com.example.balancify.domain.model

import com.example.balancify.core.util.DateAsLongSerializer
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.serialization.Serializable
import java.util.Date

@IgnoreExtraProperties
@Serializable
data class NotificationModel(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val link: String? = null,
    val type: NotificationType = NotificationType.FRIEND_REQUEST,
    val userReadFlag: Map<String, Boolean> = emptyMap(),
    val ownerIds: List<String> = emptyList(),
    @Serializable(with = DateAsLongSerializer::class)
    @ServerTimestamp val createdAt: Date? = null,
) {
    fun isUnread(userId: String): Boolean {
        return userReadFlag[userId] == false
    }
}