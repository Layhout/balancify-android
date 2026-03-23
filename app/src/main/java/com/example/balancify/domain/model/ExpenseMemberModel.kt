package com.example.balancify.domain.model

import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.serialization.Serializable

@IgnoreExtraProperties
@Serializable
data class ExpenseMemberModel(
    override val id: String = "",
    override val email: String = "",
    override val imageUrl: String = "",
    override val name: String = "",
    override val notiToken: String = "",
    override val profileBgColor: String = "",
    override val referralCode: String = "",
    override val subNoti: Boolean = false,
    val amount: Double = 0.0,
    val settledAmount: Double = 0.0,
) : User