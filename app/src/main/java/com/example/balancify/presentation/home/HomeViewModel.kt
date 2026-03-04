package com.example.balancify.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {
    private val _state = MutableStateFlow(HomeState())

    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeState()
    )

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnToggleFabClick -> {
                _state.update {
                    it.copy(
                        toggleFab = !it.toggleFab
                    )
                }
            }

            is HomeAction.OnCreateGroupClick -> {
                // Navigate to create group screen
            }

            is HomeAction.OnCreateExpenseClick -> {
                // Navigate to create expense screen
            }
        }
    }

}