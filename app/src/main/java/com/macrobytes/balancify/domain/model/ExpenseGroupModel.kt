package com.macrobytes.balancify.domain.model

import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.serialization.Serializable

@IgnoreExtraProperties
@Serializable
data class ExpenseGroupModel(
    val id: String = "",
    val name: String = "",
) {
    companion object {
        fun fromGroupModel(group: GroupModel): ExpenseGroupModel {
            return ExpenseGroupModel(
                id = group.id,
                name = group.name,
            )
        }
    }
}