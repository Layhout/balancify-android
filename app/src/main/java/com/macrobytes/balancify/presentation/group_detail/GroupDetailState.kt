package com.macrobytes.balancify.presentation.group_detail

import com.macrobytes.balancify.domain.model.ExpenseModel
import com.macrobytes.balancify.domain.model.GroupModel
import com.macrobytes.balancify.domain.model.UserModel
import com.google.firebase.firestore.DocumentSnapshot

data class GroupDetailState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val canLoadMore: Boolean = false,
    val showDropdown: Boolean = false,
    val showMemberBottomSheet: Boolean = false,
    val enableAllAction: Boolean = true,
    val isCreateByLocalUser: Boolean = false,
    val isLeaveBottomSheetVisible: Boolean = false,
    val localUser: UserModel? = null,
    val group: GroupModel = GroupModel(),
    val expenses: List<ExpenseModel> = emptyList(),
    val lastExpenseDoc: DocumentSnapshot? = null,
)
