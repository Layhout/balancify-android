package com.macrobytes.balancify.domain.model

sealed interface FoundItemData {
    data class Friend(val data: FriendModel) : FoundItemData
    data class Group(val data: GroupModel) : FoundItemData
}