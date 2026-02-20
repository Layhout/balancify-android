package com.example.balancify.presentation.group_form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class GroupFormViewModel : ViewModel() {
    private val _state = MutableStateFlow(GroupFormState())

    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = GroupFormState()
    )

    private val _events = Channel<GroupFormEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: GroupFormAction) {
        when (action) {
            is GroupFormAction.OnAddMemberClick -> {
                _events.trySend(GroupFormEvent.OnAddMemberClicked)
            }

            is GroupFormAction.OnAddMember -> {
                _state.update {
                    it.copy(
                        members = it.members + action.member
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
        }
    }

}