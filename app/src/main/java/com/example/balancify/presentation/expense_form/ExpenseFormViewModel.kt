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
            }

            _state.update {
                it.copy(
                    icon = ExpenseIcon.entries[(1..ExpenseIcon.entries.lastIndex).random()],
                    iconBgColor = BG_COLORS[(0..BG_COLORS.lastIndex).random()],
                    localUser = localUser,
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

    private fun normalizeMemberAmount(amount: Double) {
        if (_state.value.members.isEmpty()) {
            _state.update {
                it.copy(
                    amount = amount.toString()
                )
            }
            return
        }

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
                    amount = amount.toString(),
                    members = newMemberList
                )
            }

        } else {
            _state.update {
                it.copy(
                    amount = amount.toString(),
                )
            }
        }
    }

    fun onAction(action: ExpenseFormAction) {
        when (action) {
            is ExpenseFormAction.OnMemberOptionChanged -> {
                _state.update {
                    it.copy(
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
                normalizeMemberAmount(_state.value.amount.toDoubleOrNull() ?: 0.0)
            }

            is ExpenseFormAction.OnSplitOptionChanged -> {
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

            is ExpenseFormAction.OnIconChanged -> {
                _state.update {
                    it.copy(
                        icon = action.icon
                    )
                }
            }

            is ExpenseFormAction.OnIconBgColorChanged -> {
                _state.update {
                    it.copy(
                        iconBgColor = action.bgColor
                    )
                }
            }

            is ExpenseFormAction.OnNameChanged -> {
                _state.update {
                    it.copy(
                        name = action.name
                    )
                }
            }

            is ExpenseFormAction.OnAmountChanged -> {
                normalizeMemberAmount(action.amount.toDoubleOrNull() ?: 0.0)
            }

            is ExpenseFormAction.OnMemberAmountChanged -> {
                val isValidAmount = action.amount.matches(Regex("^-?\\d*(\\.\\d{0,2})?$"))
                if (!isValidAmount) return

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

            is ExpenseFormAction.OnMemberRemoved -> {
                _state.update {
                    it.copy(
                        members = it.members.filterIndexed { index, _ ->
                            index != action.index
                        }
                    )
                }
            }

            is ExpenseFormAction.OnAddMember -> {
                _state.update {
                    it.copy(
                        members = action.members + it.members
                    )
                }

                normalizeMemberAmount(
                    _state.value.amount.toDoubleOrNull() ?: 0.0,
                )
            }

            is ExpenseFormAction.OnCollectFlag -> {
                val searchResult = globalAppStateManager.getSearchResult()

                searchResult?.let {
                    if (it is SearchResult.Friend) {
                        onAction(
                            ExpenseFormAction.OnAddMember(
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
                            ExpenseFormAction.OnAddMember(
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

            is ExpenseFormAction.OnAddMemberClicked -> {
                _events.trySend(ExpenseFormEvent.OnAddMemberClicked)
            }
        }
    }
}
