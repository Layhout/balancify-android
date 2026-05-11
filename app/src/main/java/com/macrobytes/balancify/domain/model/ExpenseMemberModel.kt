package com.macrobytes.balancify.domain.model

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
) : User {
    companion object {
        fun fromUserModel(user: User, amount: Double, settledAmount: Double): ExpenseMemberModel {
            return ExpenseMemberModel(
                id = user.id,
                email = user.email,
                imageUrl = user.imageUrl,
                name = user.name,
                notiToken = user.notiToken,
                profileBgColor = user.profileBgColor,
                referralCode = user.referralCode,
                subNoti = user.subNoti,
                amount = amount,
                settledAmount = settledAmount
            )
        }
    }

}