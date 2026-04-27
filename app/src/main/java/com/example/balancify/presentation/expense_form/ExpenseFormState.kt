package com.example.balancify.presentation.expense_form

import com.example.balancify.core.constant.BG_COLORS
import com.example.balancify.domain.model.ExpenseGroupModel
import com.example.balancify.domain.model.ExpenseIcon
import com.example.balancify.domain.model.ExpenseMemberModel
import com.example.balancify.domain.model.MemberOption
import com.example.balancify.domain.model.SplitOption
import com.example.balancify.domain.model.UserModel

data class ExpenseFormState(
    val isLoading: Boolean = false,
    val isNameInvalid: Boolean = false,
    val isMemberInvalid: Boolean = false,
    val amountErrorMessage: String? = null,
    val isEditing: Boolean = false,
    val showIconBottomSheet: Boolean = false,
    val isEnableAllAction: Boolean = true,
    val amount: String = "",
    val name: String = "",
    val icon: ExpenseIcon = ExpenseIcon.BOOKMARK,
    val iconBgColor: String = BG_COLORS[0],
    val memberOption: MemberOption = MemberOption.FRIEND,
    val splitOption: SplitOption = SplitOption.SPLIT_EQUALLY,
    val members: List<ExpenseMemberModel> = emptyList(),
    val localUser: UserModel? = null,
    val paidBy: UserModel? = null,
    val previousPayer: UserModel? = null,
    val group: ExpenseGroupModel? = null,
)