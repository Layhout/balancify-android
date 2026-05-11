package com.macrobytes.balancify.presentation.group_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.macrobytes.balancify.core.constant.GlobalAppStateFlag
import com.macrobytes.balancify.core.manager.GlobalAppStateManager
import com.macrobytes.balancify.domain.use_case.expense.ExpenseUseCases
import com.macrobytes.balancify.domain.use_case.group.GroupUseCases
import com.macrobytes.balancify.domain.use_case.user.UserUseCases
import com.macrobytes.balancify.navigatin.Routes
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GroupDetailViewModel(
    private val groupUseCases: GroupUseCases,
    private val userUseCases: UserUseCases,
    private val expenseUseCases: ExpenseUseCases,
    private val globalAppStateManager: GlobalAppStateManager,
    private val handle: SavedStateHandle,
) : ViewModel() {
    private val _state = MutableStateFlow(GroupDetailState())
    val state = _state.onStart { loadData() }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GroupDetailState()
    )

    private val _events = Channel<GroupDetailEvent>()
    val events = _events.receiveAsFlow()

    private fun alertError(message: String?) {
        _events.trySend(
            GroupDetailEvent.OnError(message ?: "Unknown error")
        )
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    enableAllAction = false,
                )
            }

            val id = handle.toRoute<Routes.GroupDetail>().id
            val result = groupUseCases.getGroupDetail(id)
            if (result.isFailure) {
                alertError(result.exceptionOrNull()?.message)
                return@launch
            }

            val userResult = userUseCases.getLocalUser()
            if (userResult.isFailure) {
                alertError(userResult.exceptionOrNull()?.message)
                return@launch
            }

            val expensesResult =
                expenseUseCases.getExpensesForGroup(_state.value.lastExpenseDoc, groupId = id)
            if (expensesResult.isFailure) {
                alertError(expensesResult.exceptionOrNull()?.message)
                return@launch
            }

            _state.update {
                it.copy(
                    localUser = userResult.getOrNull()!!,
                    isLoading = false,
                    enableAllAction = true,
                    group = result.getOrNull()!!,
                    isCreateByLocalUser =
                        result.getOrNull()!!.createdBy
                                == userResult.getOrNull()!!.id,
                    expenses = expensesResult.getOrNull()?.data ?: emptyList(),
                    canLoadMore = expensesResult.getOrNull()?.canLoadMore ?: false,
                )
            }
        }
    }

    private fun loadMoreExpense(lastDoc: DocumentSnapshot? = null) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                )
            }

            val expensesResult =
                expenseUseCases.getExpensesForGroup(
                    lastDoc,
                    groupId = _state.value.group.id
                )
            if (expensesResult.isFailure) {
                alertError(expensesResult.exceptionOrNull()?.message)
                return@launch
            }

            _state.update {
                it.copy(
                    isLoading = false,
                    lastExpenseDoc = expensesResult.getOrNull()?.lastDoc,
                    canLoadMore = expensesResult.getOrNull()?.canLoadMore ?: false,
                    expenses = if (it.lastExpenseDoc != null) it.expenses + (expensesResult.getOrNull()?.data
                        ?: emptyList()) else (expensesResult.getOrNull()?.data ?: emptyList()),
                )
            }
        }
    }

    fun onAction(action: GroupDetailAction) {
        when (action) {
            GroupDetailAction.OnRefresh -> loadData()
            GroupDetailAction.OnLoadMore -> loadMoreExpense(_state.value.lastExpenseDoc)
            GroupDetailAction.OnDropdownMenuToggle -> {
                _state.update {
                    it.copy(
                        showDropdown = !it.showDropdown
                    )
                }
            }

            GroupDetailAction.OnMemberBottomSheetToggle -> {
                _state.update {
                    it.copy(
                        showMemberBottomSheet = !it.showMemberBottomSheet
                    )
                }
            }

            GroupDetailAction.OnLeaveGroupClick -> {
                _state.update {
                    it.copy(
                        isLeaveBottomSheetVisible = true
                    )
                }
            }

            GroupDetailAction.OnLeaveConfirmClick -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            enableAllAction = false
                        )
                    }
                    val result = if (_state.value.group.members.size == 1)
                        groupUseCases.deleteGroup(_state.value.group.id)
                    else
                        groupUseCases.leaveGroup(_state.value.group.id, _state.value.group.members)

                    if (result.isFailure) {
                        alertError(result.exceptionOrNull()?.message)
                        _state.update {
                            it.copy(
                                enableAllAction = true
                            )
                        }
                        return@launch
                    }

                    _state.update {
                        it.copy(
                            enableAllAction = true,
                            isLeaveBottomSheetVisible = false
                        )
                    }
                    globalAppStateManager.setFlag(
                        GlobalAppStateFlag.GROUP_LIST_SHOULD_REFRESH,
                        true
                    )
                    _events.trySend(GroupDetailEvent.OnLeaveGroup)
                }
            }

            GroupDetailAction.OnLeaveDismiss -> {
                _state.update {
                    it.copy(
                        isLeaveBottomSheetVisible = false
                    )
                }
            }

            is GroupDetailAction.OnCollectFlag -> {
                val refreshFlag =
                    globalAppStateManager.pullFlag(GlobalAppStateFlag.GROUP_DID_UPDATE)
                            || globalAppStateManager.pullFlag(GlobalAppStateFlag.EXPENSE_DID_UPDATE)
                            || globalAppStateManager.pullFlag(
                        GlobalAppStateFlag.EXPENSE_LIST_SHOULD_REFRESH
                    )

                if (refreshFlag)
                    onAction(GroupDetailAction.OnRefresh)
            }
        }
    }
}