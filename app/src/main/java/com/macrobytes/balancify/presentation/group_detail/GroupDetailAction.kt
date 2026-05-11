package com.macrobytes.balancify.presentation.group_detail

sealed interface GroupDetailAction {
    data object OnRefresh : GroupDetailAction
    data object OnLoadMore : GroupDetailAction
    data object OnDropdownMenuToggle : GroupDetailAction
    data object OnMemberBottomSheetToggle : GroupDetailAction
    data object OnLeaveGroupClick : GroupDetailAction
    data object OnLeaveDismiss : GroupDetailAction
    data object OnLeaveConfirmClick : GroupDetailAction
    data object OnCollectFlag : GroupDetailAction
}