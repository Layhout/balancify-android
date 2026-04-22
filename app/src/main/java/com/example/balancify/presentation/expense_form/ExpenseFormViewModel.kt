package com.example.balancify.presentation.expense_form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.balancify.core.constant.BG_COLORS
import com.example.balancify.core.constant.SearchResult
import com.example.balancify.core.manager.GlobalAppStateManager
import com.example.balancify.domain.model.ExpenseIcon
import com.example.balancify.domain.model.ExpenseMemberModel
import com.example.balancify.domain.model.MemberOption
import com.example.balancify.domain.model.SplitOption
import com.example.balancify.domain.model.UserModel
import com.example.balancify.domain.use_case.user.UserUseCases
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
    private val userUseCases: UserUseCases,
    private val globalAppStateManager: GlobalAppStateManager,
) : ViewModel() {
    private val _state = MutableStateFlow(ExpenseFormState())
    val state = _state.onStart { loadData() }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ExpenseFormState(),
    )

    private val _events = Channel<ExpenseFormEvent>()
    val events = _events.receiveAsFlow()

    private fun alertError(message: String?) {
        _events.trySend(
            ExpenseFormEvent.OnError(message ?: "Unknown error")
        )
    }

    private fun loadData() {
        viewModelScope.launch {
            var localUser: UserModel? = _state.value.localUser

            if (localUser == null) {
                val result = userUseCases.getLocalUser()

                if (result.isSuccess) {
                    localUser = result.getOrNull()
                }

                _state.update {
                    it.copy(
                        icon = ExpenseIcon.entries[(1..ExpenseIcon.entries.lastIndex).random()],
                        iconBgColor = BG_COLORS[(0..BG_COLORS.lastIndex).random()],
                        localUser = localUser,
                        paidBy = localUser,
                        members = listOf(
                            ExpenseMemberModel.fromUserModel(
                                user = localUser!!,
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
                    it.copy(
                        paidBy = it.localUser,
                        memberOption = action.option,
                        members = if (action.option == MemberOption.FRIEND) listOf(
                            ExpenseMemberModel.fromUserModel(
                                user = _state.value.localUser!!,
                                amount = 0.0,
                                settledAmount = 0.0
                            )
                        ) else emptyList(),
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
                        else action.members
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
                        onAction(
                            OnAddMember(
                                (it as SearchResult.Group).data.members.map { member ->
                                    ExpenseMemberModel.fromUserModel(
                                        member,
                                        amount = 0.0,
                                        settledAmount = 0.0,
                                    )
                                }
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
                    )
                }

                var isValid = true

                if (_state.value.name.isBlank()) {
                    _state.update { it.copy(isNameInvalid = true) }
                    isValid = false
                }

                if (_state.value.members.size > 10) {
                    _state.update { it.copy(isMemberInvalid = true) }
                    isValid = false
                }

                val amount = _state.value.amount.toDoubleOrNull() ?: 0.0

                if (amount <= 0) {
                    _state.update { it.copy(isAmountInvalid = true) }
                    isValid = false
                }

                if (!isValid) return


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
