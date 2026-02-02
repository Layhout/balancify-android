package com.example.balancify.presentation.friend

import com.example.balancify.domain.model.FriendModel

data class FriendState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val canLoadMore: Boolean = true,
    val friends: List<FriendModel> = emptyList(),
)
