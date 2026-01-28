package com.example.balancify.domain.model

import com.example.balancify.core.util.DateAsLongSerializer
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class UserModel(
    @DocumentId val documentId: String = "",
    val id: String = "",
    val email: String = "",
    val imageUrl: String = "",
    val name: String = "",
    val notiToken: String = "",
    val profileBgColor: String = "",
    val referralCode: String = "",
    @Serializable(with = DateAsLongSerializer::class)
    @ServerTimestamp val createdAt: Date? = null,
)
