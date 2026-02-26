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

    private fun loadData(isLoading: Boolean = true) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = isLoading
                )
            }

            val id = handle.toRoute<Routes.GroupDetail>().id
            val result = groupUseCases.getGroupDetail(id)
            if (result.isSuccess) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        group = result.getOrNull()!!
                    )
                }
            }
        }
    }

    fun onAction(action: GroupDetailAction) {
        when (action) {
            GroupDetailAction.OnRefresh -> TODO()
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
        }
    }
}