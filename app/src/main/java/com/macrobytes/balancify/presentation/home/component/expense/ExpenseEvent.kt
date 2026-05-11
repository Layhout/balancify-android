package com.macrobytes.balancify.presentation.home.component.expense

sealed interface ExpenseEvent {
    data class OnError(val message: String) : ExpenseEvent
}