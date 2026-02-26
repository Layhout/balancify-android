package com.example.balancify.presentation.group_detail

import com.example.balancify.domain.model.GroupModel

data class GroupDetailState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val canLoadMore: Boolean = false,
    val showDropdown: Boolean = false,
    val showMemberBottomSheet: Boolean = false,
    val group: GroupModel = GroupModel(),
)
