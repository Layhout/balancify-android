package com.macrobytes.balancify.presentation.group_form

import com.macrobytes.balancify.domain.model.UserModel

data class GroupFormState(
    val isLoading: Boolean = false,
    val isNameInvalid: Boolean = false,
    val isMemberInvalid: Boolean = false,
    val isEditing: Boolean = false,
    val isEnableAllAction: Boolean = true,
    val name: String = "",
    val description: String = "",
    val members: List<UserModel> = emptyList(),
    val localUser: UserModel? = null,
)