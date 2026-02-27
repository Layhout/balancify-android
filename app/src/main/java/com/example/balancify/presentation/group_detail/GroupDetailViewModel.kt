package com.example.balancify.presentation.group_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.balancify.domain.use_case.group.GroupUseCases
import com.example.balancify.navigatin.Routes
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
    private val handle: SavedStateHandle
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
            }
            
            _state.update {
                it.copy(
                    isLoading = false,
                    enableAllAction = true,
                    group = result.getOrNull()!!
                )
            }
        }
    }

    fun onAction(action: GroupDetailAction) {
        when (action) {
            GroupDetailAction.OnRefresh -> loadData()
            GroupDetailAction.OnLoadMore -> TODO()
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
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            enableAllAction = false
                        )
                    }
                    val result = if (_state.value.group.members.size == 1)
                        groupUseCases.deleteGroup(_state.value.group.id)
                    else
                        groupUseCases.leaveGroup(_state.value.group.id)

                    if (result.isFailure) {
                        alertError(result.exceptionOrNull()?.message)
                    } else {
                        _state.update {
                            it.copy(
                                enableAllAction = true
                            )
                        }
                    }
                }
            }
        }
    }
}