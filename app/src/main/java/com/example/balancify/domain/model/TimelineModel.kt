package com.example.balancify.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TimelineModel(
    val createdAt: Long = 0,
    val createdBy: UserModel = UserModel(),
    val events: String = "",
)
