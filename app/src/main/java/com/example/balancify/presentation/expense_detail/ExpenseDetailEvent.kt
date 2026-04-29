package com.example.balancify.presentation.expense_detail

sealed interface ExpenseDetailEvent {
    data class OnError(val message: String) : ExpenseDetailEvent
    data object OnDeletionSuccess : ExpenseDetailEvent
}