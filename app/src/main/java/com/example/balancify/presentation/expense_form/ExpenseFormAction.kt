package com.example.balancify.presentation.expense_form

import com.example.balancify.domain.model.ExpenseGroupModel
import com.example.balancify.domain.model.ExpenseIcon
import com.example.balancify.domain.model.ExpenseMemberModel
import com.example.balancify.domain.model.MemberOption
import com.example.balancify.domain.model.SplitOption

sealed interface ExpenseFormAction {
    data class OnMemberOptionChange(val option: MemberOption) : ExpenseFormAction
    data class OnSplitOptionChange(val option: SplitOption) : ExpenseFormAction
    data object OnIconFormBottomSheetToggle : ExpenseFormAction
    data class OnIconChange(val icon: ExpenseIcon) : ExpenseFormAction
    data class OnIconBgColorChange(val bgColor: String) : ExpenseFormAction
    data class OnNameChange(val name: String) : ExpenseFormAction
    data class OnAmountChange(val amount: String) : ExpenseFormAction
    data class OnMemberAmountChange(val index: Int, val amount: String) : ExpenseFormAction
    data class OnMemberRemove(val index: Int) : ExpenseFormAction
    data class OnAddMember(
        val members: List<ExpenseMemberModel>,
        val group: ExpenseGroupModel? = null
    ) : ExpenseFormAction

    data object OnCollectFlag : ExpenseFormAction
    data object OnAddMemberClick : ExpenseFormAction
    data object OnSaveClick : ExpenseFormAction
    data class OnPaidByChange(val id: String) : ExpenseFormAction
}
