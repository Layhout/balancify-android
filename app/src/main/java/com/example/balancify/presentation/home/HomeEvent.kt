package com.example.balancify.presentation.home

sealed interface HomeEvent {
    data object OnRefreshGroup : HomeEvent
    data object OnRefreshExpense : HomeEvent
}