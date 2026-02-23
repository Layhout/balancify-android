package com.example.balancify.domain.model

import com.example.balancify.core.util.DateAsLongSerializer
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class GroupModel(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    @Serializable(with = DateAsLongSerializer::class)
    @ServerTimestamp val createdAt: Date? = null,
    val createdBy: String = "",
    val members: List<UserModel> = emptyList(),
    val memberIds: List<String> = emptyList(),
)