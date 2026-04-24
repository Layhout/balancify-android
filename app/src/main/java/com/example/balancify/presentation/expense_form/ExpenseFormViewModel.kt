package com.example.balancify.presentation.expense_form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.balancify.core.constant.BG_COLORS
import com.example.balancify.core.constant.GlobalAppStateFlag
import com.example.balancify.core.constant.SearchResult
import com.example.balancify.core.ext.toCleanString
import com.example.balancify.core.manager.GlobalAppStateManager
import com.example.balancify.domain.model.ExpenseGroupModel
import com.example.balancify.domain.model.ExpenseIcon
import com.example.balancify.domain.model.ExpenseMemberModel
import com.example.balancify.domain.model.ExpenseModel
import com.example.balancify.domain.model.MemberOption
import com.example.balancify.domain.model.SplitOption
import com.example.balancify.domain.model.UserModel
import com.example.balancify.domain.use_case.expense.ExpenseUseCases
import com.example.balancify.domain.use_case.user.UserUseCases
import com.example.balancify.navigatin.Routes
import com.example.balancify.presentation.expense_form.ExpenseFormAction.OnAddMember
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class ExpenseFormViewModel(
    private val expenseUseCases: ExpenseUseCases,
    private val userUseCases: UserUseCases,
    private val globalAppStateManager: GlobalAppStateManager,
    private val handle: SavedStateHandle,
) : ViewModel() {
    private val _state = MutableStateFlow(ExpenseFormState())
    val state = _state.onStart { loadData() }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ExpenseFormState(),
    )

    private val _events = Channel<ExpenseFormEvent>()
    val events = _events.receiveAsFlow()

    private val formPayload: ExpenseModel
        get() = ExpenseModel(
            name = _state.value.name,
            amount = _state.value.amount.toDouble(),
            icon = _state.value.icon.value,
            iconBgColor = _state.value.iconBgColor,
            memberOption = _state.value.memberOption,
            splitOption = _state.value.splitOption,
            group = _state.value.group,
            paidBy = _state.value.paidBy!!,
        )

    private fun alertError(message: String?) {
        _events.trySend(
            ExpenseFormEvent.OnError(message ?: "Unknown error")
        )
    }

    private fun loadData() {
        val localUser: UserModel? = _state.value.localUser

        if (localUser != null) return

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = false,
                    isEditing = true,
                    isEnableAllAction = false,
                )
            }

            val userResult = userUseCases.getLocalUser()
            if (userResult.isFailure) {
                alertError(userResult.exceptionOrNull()?.message)
                return@launch
            }

            val expenseId = handle.toRoute<Routes.ExpenseForm>().id

            if (expenseId != null) {
                val result = expenseUseCases.getExpenseDetail(expenseId)
                if (result.isFailure) {
                    alertError(result.exceptionOrNull()?.message)
                    return@launch
                }

                val detail = result.getOrNull()!!

                _state.update {
                    it.copy(
                        icon = ExpenseIcon.fromValue(detail.icon),
                        iconBgColor = detail.iconBgColor,
                        localUser = userResult.getOrNull(),
                        paidBy = detail.paidBy,
                        members = detail.member.values.toList(),
                        name = detail.name,
                        amount = detail.amount.toCleanString(),
                        memberOption = detail.memberOption,
                        splitOption = detail.splitOption,
                        group = detail.group,
                        previousPayer = detail.paidBy,

                        )
                }

            } else {
                _state.update {
                    it.copy(
                        icon = ExpenseIcon.entries[(1..ExpenseIcon.entries.lastIndex).random()],
                        iconBgColor = BG_COLORS[(0..BG_COLORS.lastIndex).random()],
                        localUser = userResult.getOrNull(),
                        paidBy = userResult.getOrNull(),
                        members = listOf(
                            ExpenseMemberModel.fromUserModel(
                                user = userResult.getOrNull() ?: UserModel(),
                                amount = 0.0,
                                settledAmount = 0.0
                            )
                        ),
                    )
                }
            }
        }
    }

    private fun normalizeMemberAmount(amountString: String) {
        if (_state.value.members.isEmpty()) {
            _state.update {
                it.copy(
                    amount = amountString
                )
            }
            return
        }

        val amount = amountString.toDoubleOrNull() ?: 0.0

        if (_state.value.splitOption == SplitOption.SPLIT_EQUALLY) {
            val df = DecimalFormat("#.00", DecimalFormatSymbols(Locale.US))
            df.roundingMode = RoundingMode.HALF_UP

            val newMemberList = _state.value.members.map {
                val newAmount =
                    df.format(amount / _state.value.members.size).toDoubleOrNull() ?: 0.0
                it.copy(
                    amount = newAmount
                )
            }.toMutableList()

            val remainingAmount =
                df.format(amount - newMemberList.sumOf { it.amount }).toDoubleOrNull() ?: 0.0

            newMemberList[newMemberList.lastIndex] =
                newMemberList[newMemberList.lastIndex].copy(
                    amount = newMemberList[newMemberList.lastIndex].amount + remainingAmount
                )

            _state.update {
                it.copy(
                    amount = amountString,
                    members = newMemberList
                )
            }

        } else {
            _state.update {
                it.copy(
                    amount = amountString,
                )
            }
        }
    }

    fun onAction(action: ExpenseFormAction) {
        when (action) {
            is ExpenseFormAction.OnMemberOptionChange -> {
                _state.update {
                    var newState = it
                    if (action.option == MemberOption.FRIEND) {
                        newState = newState.copy(
                            group = null,
                            members = listOf(
                                ExpenseMemberModel.fromUserModel(
                                    user = _state.value.localUser!!,
                                    amount = 0.0,
                                    settledAmount = 0.0
                                )
                            )
                        )
                    } else {
                        newState = newState.copy(
                            members = emptyList(),
                        )
                    }

                    newState.copy(
                        paidBy = newState.localUser,
                        memberOption = action.option,
                    )
                }
                normalizeMemberAmount(_state.value.amount)
            }

            is ExpenseFormAction.OnSplitOptionChange -> {
                _state.update {
                    it.copy(
                        splitOption = action.option
                    )
                }
            }

            is ExpenseFormAction.OnIconFormBottomSheetToggle -> {
                _state.update {
                    it.copy(
                        showIconBottomSheet = !it.showIconBottomSheet
                    )
                }
            }

            is ExpenseFormAction.OnIconChange -> {
                _state.update {
                    it.copy(
                        icon = action.icon
                    )
                }
            }

            is ExpenseFormAction.OnIconBgColorChange -> {
                _state.update {
                    it.copy(
                        iconBgColor = action.bgColor
                    )
                }
            }

            is ExpenseFormAction.OnNameChange -> {
                _state.update {
                    it.copy(
                        name = action.name
                    )
                }
            }

            is ExpenseFormAction.OnAmountChange -> {
                normalizeMemberAmount(action.amount)
            }

            is ExpenseFormAction.OnMemberAmountChange -> {
                val memberToUpdate = _state.value.members[action.index]
                val newMemberList = _state.value.members.toMutableList()
                newMemberList[action.index] = memberToUpdate.copy(
                    amount = action.amount.toDoubleOrNull() ?: 0.0
                )

                _state.update {
                    it.copy(
                        members = newMemberList,
                        splitOption = SplitOption.CUSTOM,
                    )
                }
            }

            is ExpenseFormAction.OnMemberRemove -> {
                _state.update {
                    it.copy(
                        members = it.members.filterIndexed { index, _ ->
                            index != action.index
                        }
                    )
                }
            }

            is OnAddMember -> {
                _state.update {
                    it.copy(
                        members = if (_state.value.memberOption == MemberOption.FRIEND)
                            action.members + it.members
                        else action.members,
                        group = action.group,
                    )
                }

                normalizeMemberAmount(
                    _state.value.amount,
                )
            }

            is ExpenseFormAction.OnCollectFlag -> {
                val searchResult = globalAppStateManager.getSearchResult()
                searchResult?.let {
                    if (it is SearchResult.Friend) {
                        onAction(
                            OnAddMember(
                                listOf(
                                    ExpenseMemberModel.fromUserModel(
                                        it.data.user!!,
                                        amount = 0.0,
                                        settledAmount = 0.0,
                                    )
                                )
                            )
                        )
                    } else {
                        val data = (it as SearchResult.Group).data
                        onAction(
                            OnAddMember(
                                data.members.map { member ->
                                    ExpenseMemberModel.fromUserModel(
                                        member,
                                        amount = 0.0,
                                        settledAmount = 0.0,
                                    )
                                },
                                ExpenseGroupModel(
                                    id = data.id,
                                    name = data.name,
                                )
                            )
                        )
                    }
                }
            }

            is ExpenseFormAction.OnAddMemberClick -> {
                _events.trySend(ExpenseFormEvent.OnAddMemberClicked)
            }

            is ExpenseFormAction.OnSaveClick -> {
                _state.update {
                    it.copy(
                        isNameInvalid = false,
                        isMemberInvalid = false,
                        amountErrorMessage = null,
                    )
                }

                var isValid = true

                if (_state.value.name.isBlank()) {
                    _state.update { it.copy(isNameInvalid = true) }
                    isValid = false
                }

                if (_state.value.members.isEmpty() || _state.value.members.size > 10) {
                    _state.update { it.copy(isMemberInvalid = true) }
                    isValid = false
                }

                val amount = _state.value.amount.toDoubleOrNull() ?: 0.0

                if (amount <= 0.0) {
                    _state.update { it.copy(amountErrorMessage = "Amount is required") }
                    isValid = false
                }

                if (amount != _state.value.members.sumOf { it.amount }) {
                    _state.update {
                        it.copy(
                            amountErrorMessage = "Amount and members expense amount does not match"
                        )
                    }
                    isValid = false
                }

                if (!isValid) return

                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isLoading = true,
                            isEnableAllAction = false,
                        )
                    }

                    val result = if (_state.value.isEditing) expenseUseCases.createExpense(
                        formPayload,
                        _state.value.members,
                    ) else expenseUseCases.updateExpense(
                        id = handle.toRoute<Routes.GroupFrom>().id!!,
                        formPayload,
                        _state.value.members,
                        _state.value.previousPayer,
                    )

                    if (result.isFailure) {
                        alertError(result.exceptionOrNull()?.message)
                    } else {
                        if (_state.value.isEditing) {
                            globalAppStateManager.setFlag(
                                GlobalAppStateFlag.EXPENSE_LIST_SHOULD_REFRESH,
                                true
                            )
                        } else {
                            globalAppStateManager.setFlag(
                                GlobalAppStateFlag.EXPENSE_LIST_SHOULD_REFRESH,
                                true
                            )
                        }
                        _events.trySend(ExpenseFormEvent.OnSaveSuccess)
                    }

                    _state.update {
                        it.copy(
                            isLoading = false,
                            isEnableAllAction = true,
                        )
                    }
                }
            }

            is ExpenseFormAction.OnPaidByChange -> {
                _state.update {
                    val found = it.members.find { member ->
                        member.id == action.id
                    }

                    it.copy(
                        paidBy = UserModel.fromExpenseMemberModel(found!!)
                    )
                }
            }
        }
    }
}
