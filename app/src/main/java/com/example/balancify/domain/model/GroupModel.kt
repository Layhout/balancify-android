package com.example.balancify.domain.model

import android.os.Parcelable
import com.example.balancify.core.util.DateAsLongSerializer
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.util.Date

@IgnoreExtraProperties
@Serializable
@Parcelize
data class GroupModel(
    val id: String = "",
    val name: String = "--",
    val description: String = "--",
    @Serializable(with = DateAsLongSerializer::class)
    @ServerTimestamp val createdAt: Date? = null,
    val createdBy: String = "",
    val members: List<UserModel> = emptyList(),
    val memberIds: List<String> = emptyList(),
) : Parcelable {
    fun getOwner(): String {
        return members.find { it.id == createdBy }?.name ?: ""
    }
}