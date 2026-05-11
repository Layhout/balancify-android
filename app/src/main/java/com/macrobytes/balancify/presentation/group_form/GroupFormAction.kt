package com.macrobytes.balancify.presentation.group_form

import com.macrobytes.balancify.domain.model.FriendModel

sealed interface GroupFormAction {
    data object OnAddMemberClick : GroupFormAction
    data class OnAddMember(val member: FriendModel) : GroupFormAction
    data class OnRemoveMemberClick(val id: String) : GroupFormAction
    data class OnNameChange(val name: String) : GroupFormAction
    data class OnDescriptionChange(val description: String) : GroupFormAction
    data object OnSaveClick : GroupFormAction
    data object OnCheckForSearchResult : GroupFormAction
}