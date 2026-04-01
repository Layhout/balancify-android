package com.example.balancify.presentation.expense_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.balancify.core.constant.GlobalAppStateFlag
import com.example.balancify.core.manager.GlobalAppStateManager
import com.example.balancify.domain.model.UserModel
import com.example.balancify.domain.use_case.expense.ExpenseUseCases
import com.example.balancify.domain.use_case.user.UserUseCases
import com.example.balancify.navigatin.Routes
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExpenseDetailViewModel(
    private val useCases: ExpenseUseCases,
    private val userUseCases: UserUseCases,
    private val globalAppStateManager: GlobalAppStateManager,
    private val handle: SavedStateHandle,
) : ViewModel() {
    private val _state = MutableStateFlow(ExpenseDetailState())
    val state = _state.onStart { loadData() }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ExpenseDetailState(),
    )

    private val _events = Channel<ExpenseDetailEvent>()
    val events = _events.receiveAsFlow()

    private fun alertError(message: String?) {
        _events.trySend(
            ExpenseDetailEvent.OnError(message ?: "Unknown error")
        )
    }

    private fun loadData(isLoading: Boolean = true) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = isLoading,
                    enableAllAction = false,
                )
            }

            val id = handle.toRoute<Routes.ExpenseDetail>().id
            val result = useCases.getExpenseDetail(id)
            if (result.isFailure) {
                alertError(result.exceptionOrNull()?.message)
                return@launch
            }

            var localUser: UserModel? = _state.value.localUser

            if (localUser == null) {
                val result = userUseCases.getLocalUser()

                if (result.isSuccess) {
                    localUser = result.getOrNull()
                }
            }

            val localUserMember = result.getOrNull()!!.member[localUser?.id]

            val amountToSettle =
                (localUserMember?.amount ?: 0.0) - (localUserMember?.settledAmount ?: 0.0)
            val isAlreadySettled = amountToSettle == 0.0

            _state.update {
                it.copy(
                    isLoading = false,
                    enableAllAction = true,
                    expense = result.getOrNull()!!,
                    isPaidByLocalUser =
                        result.getOrNull()!!.paidBy.id
                                == localUser?.id,
                    localUser = localUser,
                    isCreateByLocalUser =
                        result.getOrNull()!!.createdBy.id
                                == localUser?.id,
                    isAlreadySettled = isAlreadySettled,
                    settlementAmount = amountToSettle.toString()
                        .dropLastWhile { last -> last == '0' }
                        .dropLastWhile { last -> last == '.' },
                )
            }
        }
    }

    fun onAction(action: ExpenseDetailAction) {
        when (action) {
            is ExpenseDetailAction.OnRefresh -> {
                loadData(false)
            }

            is ExpenseDetailAction.OnMemberBottomSheetToggle -> {
                _state.update {
                    it.copy(
                        showMemberBottomSheet = !it.showMemberBottomSheet
                    )
                }
            }

            is ExpenseDetailAction.OnDropdownMenuToggle -> {
                _state.update {
                    it.copy(
                        showDropdown = !it.showDropdown
                    )
                }
            }

            is ExpenseDetailAction.OnConfirmDeletion -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            enableAllAction = false
                        )
                    }

                    val result = useCases.deleteExpense(state.value.expense.id)
                    if (result.isFailure) {
                        alertError(result.exceptionOrNull()?.message)
                        _state.update {
                            it.copy(
                                enableAllAction = true
                            )
                        }
                        return@launch
                    }
                    globalAppStateManager.setFlag(
                        GlobalAppStateFlag.EXPENSE_LIST_SHOULD_REFRESH,
                        true
                    )
                    _events.trySend(ExpenseDetailEvent.OnDeletionSuccess)
                }
            }

            is ExpenseDetailAction.OnDeleteBottomSheetToggle -> {
                _state.update {
                    it.copy(
                        showDeleteConfirmationBottomSheet = !it.showDeleteConfirmationBottomSheet
                    )
                }
            }

            is ExpenseDetailAction.OnSettlementBottomSheetToggle -> {
                _state.update {
                    it.copy(
                        showSettlementBottomSheet = !it.showSettlementBottomSheet
                    )
                }
            }

            is ExpenseDetailAction.OnSettlementAmountChange -> {
                _state.update {
                    it.copy(
                        settlementAmount = action.amount
                    )
                }
            }

            is ExpenseDetailAction.OnSettlementSubmit -> {
                if (_state.value.settlementAmount.isBlank()) return

                val settledAmount =
                    state.value.expense.member[state.value.localUser?.id]?.settledAmount ?: 0.0
                val amount = (_state.value.settlementAmount.toDoubleOrNull() ?: 0.0) + settledAmount

                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            enableAllAction = false,
                            isSettling = true,
                        )
                    }

                    val result = useCases.settleExpense(
                        id = _state.value.expense.id,
                        amount = amount,
                        settledAmount = _state.value.settlementAmount.toDoubleOrNull() ?: 0.0,
                        receiverName = _state.value.expense.paidBy.name,
                    )
                    if (result.isFailure) {
                        alertError(result.exceptionOrNull()?.message)
                        _state.update {
                            it.copy(
                                enableAllAction = true,
                                isSettling = false,
                            )
                        }
                        return@launch
                    }

                    val expense = result.getOrNull()!!
                    val memberMap = _state.value.expense.member.toMutableMap()
                    memberMap.compute(_state.value.localUser?.id ?: "") { _, value ->
                        value?.copy(
                            settledAmount = expense.member[_state.value.localUser?.id]
                                ?.settledAmount
                                ?: 0.0
                        )
                    }

                    _state.update {
                        it.copy(
                            showSettlementBottomSheet = !it.showSettlementBottomSheet,
                            enableAllAction = true,
                            isSettling = false,
                            expense = it.expense.copy(
                                member = memberMap,
                                timelines = expense.timelines + it.expense.timelines
                            )
                        )
                    }
                }
            }
        }
    }
}
