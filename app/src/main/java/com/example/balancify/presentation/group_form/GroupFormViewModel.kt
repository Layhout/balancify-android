package com.example.balancify.presentation.group_form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.balancify.domain.model.UserModel
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

class GroupFormViewModel(
    private val groupUseCases: GroupUseCases,
    private val handle: SavedStateHandle,
) : ViewModel() {
    private val _state = MutableStateFlow(GroupFormState())
    val state = _state.onStart { isEditGroupCheck() }.stateIn(
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

    private fun isEditGroupCheck() {
        val group = handle.toRoute<Routes.GroupFrom>().group

        group?.let {
            _state.update {
                it.copy(
                    isEditing = true,
                    name = it.name,
                    description = it.description,
                    members = it.members,
                )
            }
        }
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

            is GroupFormAction.OnSaveClick -> {
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

                if (!isValid) return

                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isLoading = true,
                            isNameInvalid = false,
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
                        _events.trySend(GroupFormEvent.OnSaveSuccess)
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