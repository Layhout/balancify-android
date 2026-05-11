package com.macrobytes.balancify.presentation.home.component.dashboard

sealed interface DashboardAction {
    data object OnRefresh : DashboardAction
}