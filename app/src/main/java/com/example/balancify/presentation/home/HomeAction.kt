package com.example.balancify.presentation.home

sealed interface HomeAction {
    data object OnToggleFabClick : HomeAction
    data object OnCreateGroupClick : HomeAction
    data object OnCreateExpenseClick : HomeAction
}