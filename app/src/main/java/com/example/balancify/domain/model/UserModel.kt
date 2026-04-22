package com.example.balancify.domain.model

import android.os.Parcelable
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@IgnoreExtraProperties
@Serializable
@Parcelize
data class UserModel(
    override val id: String = "",
    override val email: String = "",
    override val imageUrl: String = "",
    override val name: String = "",
    override val notiToken: String = "",
    override val profileBgColor: String = "",
    override val referralCode: String = "",
    override val subNoti: Boolean = false,
) : User, Parcelable {
    companion object {
        fun fromExpenseMemberModel(model: ExpenseMemberModel): UserModel {
            return UserModel(
                id = model.id,
                email = model.email,
                imageUrl = model.imageUrl,
                name = model.name,
                notiToken = model.notiToken,
                profileBgColor = model.profileBgColor,
                referralCode = model.referralCode,
                subNoti = model.subNoti
            )
        }
    }
}