package com.macrobytes.balancify.presentation.login

sealed interface LoginEvent {
    data class OnError(val message: String) : LoginEvent
}