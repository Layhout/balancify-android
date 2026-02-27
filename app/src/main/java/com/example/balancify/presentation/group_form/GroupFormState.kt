package com.example.balancify.presentation.group_form

import com.example.balancify.domain.model.FriendModel

data class GroupFormState(
    val isLoading: Boolean = false,
    val isNameInvalid: Boolean = false,
    val isMemberInvalid: Boolean = false,
    val isEditing: Boolean = false,
    val name: String = "",
    val description: String = "",
    val members: List<FriendModel> = emptyList(),
)