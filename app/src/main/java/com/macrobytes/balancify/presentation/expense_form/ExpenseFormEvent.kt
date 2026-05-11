package com.macrobytes.balancify.presentation.expense_form

sealed interface ExpenseFormEvent {
    data class OnError(val message: String) : ExpenseFormEvent
    data object OnAddMemberClicked : ExpenseFormEvent
    data object OnSaveSuccess : ExpenseFormEvent
}