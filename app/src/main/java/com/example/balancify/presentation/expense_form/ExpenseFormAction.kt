package com.example.balancify.presentation.expense_form

import com.example.balancify.domain.model.ExpenseIcon
import com.example.balancify.domain.model.ExpenseMemberModel
import com.example.balancify.domain.model.MemberOption
import com.example.balancify.domain.model.SplitOption

sealed interface ExpenseFormAction {
    data class OnMemberOptionChanged(val option: MemberOption) : ExpenseFormAction
    data class OnSplitOptionChanged(val option: SplitOption) : ExpenseFormAction
    data object OnIconFormBottomSheetToggle : ExpenseFormAction
    data class OnIconChanged(val icon: ExpenseIcon) : ExpenseFormAction
    data class OnIconBgColorChanged(val bgColor: String) : ExpenseFormAction
    data class OnNameChanged(val name: String) : ExpenseFormAction
    data class OnAmountChanged(val amount: String) : ExpenseFormAction
    data class OnMemberAmountChanged(val index: Int, val amount: String) : ExpenseFormAction
    data class OnMemberRemoved(val index: Int) : ExpenseFormAction
    data class OnAddMember(val members: List<ExpenseMemberModel>) : ExpenseFormAction
    data object OnCollectFlag : ExpenseFormAction
    data object OnAddMemberClicked : ExpenseFormAction
}
