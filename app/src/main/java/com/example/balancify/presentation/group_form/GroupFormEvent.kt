package com.example.balancify.presentation.group_form

sealed interface GroupFormEvent {
    data class OnError(val message: String) : GroupFormEvent
    data object OnAddMemberClicked : GroupFormEvent
    data object OnSaveSuccess : GroupFormEvent

}