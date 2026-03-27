package com.example.balancify.presentation.expense_form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.balancify.core.constant.BG_COLORS
import com.example.balancify.domain.model.ExpenseIcon
import com.example.balancify.domain.model.SplitOption
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.math.RoundingMode
import java.text.DecimalFormat

class ExpenseFormViewModel : ViewModel() {
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
        _state.update {
            it.copy(
                icon = ExpenseIcon.entries[(1..ExpenseIcon.entries.lastIndex).random()],
                iconBgColor = BG_COLORS[(0..BG_COLORS.lastIndex).random()],
            )
        }
    }

    private fun normalizeMemberAmount(amount: Double) {
        if (_state.value.splitOption == SplitOption.SPLIT_EQUALLY) {
            val df = DecimalFormat("#.00")
            df.roundingMode = RoundingMode.HALF_UP

            val newMemberList = _state.value.members.map {
                val newAmount = df.format(amount / _state.value.members.size).toDouble()
                it.copy(
                    amount = newAmount
                )
            }.toMutableList()

            val remainingAmount =
                df.format(amount - newMemberList.sumOf { it.amount }).toDouble()

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
                        members = emptyList(),
                    )
                }
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
                normalizeMemberAmount(action.amount.toDouble())
            }

            is ExpenseFormAction.OnMemberAmountChanged -> {
                val isValidAmount = action.amount.matches(Regex("^-?\\d*(\\.\\d{0,2})?$"))
                if (isValidAmount) return

                val memberToUpdate = _state.value.members[action.index]
                val newMemberList = _state.value.members.toMutableList()
                newMemberList[action.index] = memberToUpdate.copy(
                    amount = action.amount.toDouble()
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

                normalizeMemberAmount(_state.value.amount.toDouble())
            }
        }
    }
}