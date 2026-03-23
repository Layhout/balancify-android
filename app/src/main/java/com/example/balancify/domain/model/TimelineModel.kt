package com.example.balancify.domain.model

import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.serialization.Serializable

@IgnoreExtraProperties
@Serializable
data class TimelineModel(
    val createdAt: Long = 0,
    val createdBy: UserModel = UserModel(),
    val events: String = "",
)
