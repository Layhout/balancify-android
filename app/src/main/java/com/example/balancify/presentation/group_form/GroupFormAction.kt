package com.example.balancify.presentation.group_form

import com.example.balancify.domain.model.FriendModel

sealed interface GroupFormAction {
    data object OnAddMemberClick : GroupFormAction
    data class OnAddMember(val member: FriendModel) : GroupFormAction
    data class OnRemoveMemberClick(val id: String) : GroupFormAction
    data class OnNameChange(val name: String) : GroupFormAction
    data class OnDescriptionChange(val description: String) : GroupFormAction
}