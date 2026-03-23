package com.example.balancify.presentation.expense_detail

import com.example.balancify.domain.model.ExpenseModel
import com.example.balancify.domain.model.UserModel

data class ExpenseDetailState(
    val isRefreshing: Boolean = false,
    val canLoadMore: Boolean = false,
    val isLoading: Boolean = true,
    val enableAllAction: Boolean = false,
    val isPaidByLocalUser: Boolean = false,
    val isCreateByLocalUser: Boolean = false,
    val showDropdown: Boolean = false,
    val showMemberBottomSheet: Boolean = false,
    val showDeleteConfirmationBottomSheet: Boolean = false,
    val showSettlementBottomSheet: Boolean = false,
    val isAlreadySettled: Boolean = false,
    val isSettling: Boolean = false,
    val expense: ExpenseModel = ExpenseModel(),
    val localUser: UserModel? = null,
    val settlementAmount: String = "",
)