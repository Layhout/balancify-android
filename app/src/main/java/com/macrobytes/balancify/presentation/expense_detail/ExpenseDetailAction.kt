package com.macrobytes.balancify.presentation.expense_detail

sealed interface ExpenseDetailAction {
    data object OnRefresh : ExpenseDetailAction
    data object OnMemberBottomSheetToggle : ExpenseDetailAction
    data object OnDropdownMenuToggle : ExpenseDetailAction
    data object OnConfirmDeletion : ExpenseDetailAction
    data object OnDeleteBottomSheetToggle : ExpenseDetailAction
    data object OnSettlementBottomSheetToggle : ExpenseDetailAction
    data class OnSettlementAmountChange(val amount: String) : ExpenseDetailAction
    data object OnSettlementSubmit : ExpenseDetailAction
    data object OnCollectFlag : ExpenseDetailAction
}