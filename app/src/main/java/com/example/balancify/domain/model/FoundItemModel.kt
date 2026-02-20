package com.example.balancify.domain.model

data class FoundItemModel(
    val id: String = "",
    val imageUrl: String = "",
    val name: String = "",
    val data: FoundItemData? = null,
)

sealed interface FoundItemData {
    data class Friend(val data: FriendModel) : FoundItemData
    data class Group(val data: Any) : FoundItemData
}