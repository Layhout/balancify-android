package com.macrobytes.balancify.presentation.home.component.expense

sealed interface ExpenseAction {
    data object OnRefresh : ExpenseAction
    data object OnLoadMore : ExpenseAction
    data object OnCollectFlag : ExpenseAction
}