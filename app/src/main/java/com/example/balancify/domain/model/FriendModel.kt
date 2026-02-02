package com.example.balancify.domain.model

import com.example.balancify.core.constant.FriendStatus
import java.util.Date

data class FriendModel(
    val userId: String = "",
    val name: String = "",
    val status: FriendStatus = FriendStatus.ACCEPTED,
    val createdAt: Date = Date(),
    val nameTrigrams: List<String> = emptyList(),
    val user: UserModel = UserModel(),
)

data class PaginatedFriendsModel(
    val friends: List<FriendModel> = emptyList(),
    val canLoadMore: Boolean = false,
)