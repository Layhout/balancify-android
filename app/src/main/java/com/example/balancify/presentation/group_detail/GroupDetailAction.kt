package com.example.balancify.presentation.group_detail

sealed interface GroupDetailAction {
    data object OnRefresh : GroupDetailAction
    data object OnLoadMore : GroupDetailAction
    data object OnDropdownMenuToggle : GroupDetailAction
    data object OnMemberBottomSheetToggle : GroupDetailAction
}