package com.example.balancify.domain.model

sealed interface FoundItemData {
    data class Friend(val data: FriendModel) : FoundItemData
    data class Group(val data: Any) : FoundItemData
}