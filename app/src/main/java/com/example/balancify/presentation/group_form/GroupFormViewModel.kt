package com.example.balancify.presentation.group_form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.balancify.domain.model.UserModel
import com.example.balancify.domain.use_case.group.GroupUseCases
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GroupFormViewModel(
    private val groupUseCases: GroupUseCases
) : ViewModel() {
    private val _state = MutableStateFlow(GroupFormState())

    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = GroupFormState()
    )

    private val _events = Channel<GroupFormEvent>()
    val events = _events.receiveAsFlow()

    private fun alertError(message: String?) {
        _events.trySend(
            GroupFormEvent.OnError(message ?: "Unknown error")
        )
    }

    fun onAction(action: GroupFormAction) {
        when (action) {
            is GroupFormAction.OnAddMemberClick -> {
                _events.trySend(GroupFormEvent.OnAddMemberClicked)
            }

            is GroupFormAction.OnAddMember -> {
                if (_state.value.members.any { it.userId == action.member.userId }) {
                    return
                }

                _state.update {
                    it.copy(
                        members = listOf(action.member) + it.members
                    )
                }
            }

            is GroupFormAction.OnRemoveMemberClick -> {
                _state.update {
                    it.copy(
                        members = it.members.filter { member ->
                            member.userId != action.id
                        }
                    )
                }
            }

            is GroupFormAction.OnNameChange -> {
                _state.update {
                    it.copy(
                        name = action.name
                    )
                }
            }

            is GroupFormAction.OnDescriptionChange -> {
                _state.update {
                    it.copy(
                        description = action.description
                    )
                }
            }

            is GroupFormAction.OnCreateClick -> {
                if (_state.value.name.isBlank()) {
                    alertError("Name cannot be empty")
                    return
                }

                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isLoading = true
                        )
                    }

                    val result = groupUseCases.createGroup(
                        name = _state.value.name,
                        description = _state.value.description,
                        members = _state.value.members.map { it.user ?: UserModel() },
                    )

                    if (result.isFailure) {
                        alertError(result.exceptionOrNull()?.message)
                    } else {
                        _events.trySend(GroupFormEvent.OnCreateSuccess)
                    }

                    _state.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

}