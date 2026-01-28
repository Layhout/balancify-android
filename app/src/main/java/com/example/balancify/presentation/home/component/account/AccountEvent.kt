package com.example.balancify.presentation.home.component.account

sealed interface AccountEvent {
    data object OnLogoutSuccessful : AccountEvent
    data class OnLogoutError(val message: String) : AccountEvent
}