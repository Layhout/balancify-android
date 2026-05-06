package com.example.balancify.presentation.home.component.dashboard

sealed interface DashboardAction {
    data object OnRefresh : DashboardAction
}