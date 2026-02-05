package com.example.balancify.domain.model

import com.example.balancify.core.constant.FriendStatus
import com.example.balancify.core.util.DateAsLongSerializer
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class FriendModel(
    val userId: String = "",
    val name: String = "",
    val status: FriendStatus = FriendStatus.ACCEPTED,
    @Serializable(with = DateAsLongSerializer::class)
    @ServerTimestamp val createdAt: Date? = null,
    val nameTrigrams: List<String> = emptyList(),
    val user: UserModel? = null,
)

data class PaginatedFriendsModel(
    val friends: List<FriendModel> = emptyList(),
    val canLoadMore: Boolean = false,
    val lastDoc: DocumentSnapshot? = null,
)