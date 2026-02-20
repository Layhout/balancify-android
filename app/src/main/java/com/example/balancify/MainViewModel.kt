package com.example.balancify

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class MainViewModel : ViewModel() {
    private val _events = Channel<MainEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: MainAction) {
        when (action) {
            is MainAction.OnEmitSearchResult -> {
                _events.trySend(
                    MainEvent.OnSearchResult(
                        action.result
                    )
                )
            }
        }

    }
}