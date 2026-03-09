package com.example.balancify.presentation.home.component.expense

sealed interface ExpenseAction {
    data object OnRefresh : ExpenseAction
    data object OnLoadMore : ExpenseAction
}