package com.macrobytes.balancify.presentation.search

sealed interface SearchEvent {
    data class OnError(val message: String) : SearchEvent
}