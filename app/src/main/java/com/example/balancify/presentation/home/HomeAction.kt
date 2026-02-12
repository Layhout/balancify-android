package com.example.balancify.presentation.home

sealed interface HomeAction {
    data class OnToggleFabClick(val check: Boolean) : HomeAction
    data object OnCreateGroupClick : HomeAction
    data object OnCreateExpenseClick : HomeAction
}