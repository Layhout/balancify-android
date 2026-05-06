package com.example.balancify.presentation.home.component.dashboard

sealed interface DashboardEvent {
    data class OnError(val message: String) : DashboardEvent
}