package com.example.balancify.presentation.group_form

sealed interface GroupFormAction {
    data object OnAddMemberClick : GroupFormAction
}