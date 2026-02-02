package com.example.balancify.presentation.friend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.balancify.core.constant.RepositoryResult
import com.example.balancify.domain.use_case.friend.FriendUseCases
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FriendViewModel(
    private val friendUseCases: FriendUseCases
) : ViewModel() {
    private val _state = MutableStateFlow(FriendState())

    val state = _state
        .onStart { loadData() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = FriendState()
        )

    private val _events = Channel<FriendEvent>()
    val events = _events.receiveAsFlow()

    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = friendUseCases.getFriends(null)

            if (result is RepositoryResult.Success) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        friends = result.data.friends,
                        canLoadMore = result.data.canLoadMore,
                    )
                }
            } else {
                _events.send(
                    FriendEvent.OnError(
                        (result as RepositoryResult.Error).throwable.message ?: "Unknown error"
                    )
                )
            }
        }
    }

    fun onAction(action: FriendAction) {
        when (action) {
            is FriendAction.OnLoadMore -> {
                loadData()
            }

            FriendAction.OnAddFriendClick -> {
            }
        }
    }
}