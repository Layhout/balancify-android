package com.example.balancify.presentation.home.component.expense

sealed interface ExpenseEvent {
    data class OnError(val message: String) : ExpenseEvent
}