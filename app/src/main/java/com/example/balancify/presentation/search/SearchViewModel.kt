package com.example.balancify.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.balancify.domain.use_case.search.SearchUseCases
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchUseCases: SearchUseCases
) : ViewModel() {
    private val _state = MutableStateFlow(SearchState())

    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = SearchState()
    )

    private val _events = Channel<SearchEvent>()
    val events = _events.receiveAsFlow()

    private fun alertError(message: String?) {
        _events.trySend(
            SearchEvent.OnError(message ?: "Unknown error")
        )
    }

    private fun loadData(lastDoc: DocumentSnapshot? = null) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = searchUseCases.findFriends(lastDoc, _state.value.searchTerm)

            if (result.isSuccess) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        foundItems = if (lastDoc != null) it.foundItems + (result.getOrNull()?.data
                            ?: emptyList())
                        else (result.getOrNull()?.data ?: emptyList()),
                        canLoadMore = result.getOrNull()?.canLoadMore ?: false,
                        lastDoc = result.getOrNull()?.lastDoc,
                    )
                }
            } else {
                alertError(result.exceptionOrNull()?.message)
            }
        }
    }

    fun onAction(action: SearchAction) {
        when (action) {
            is SearchAction.OnSearchClick -> {
                _state.update {
                    it.copy(
                        isLoading = true,
                        searchTerm = action.searchTerm,
                    )
                }
                loadData()
            }

            is SearchAction.OnSearchTypeReceive -> {
                _state.value = _state.value.copy(
                    searchType = action.type
                )
            }

            is SearchAction.OnRefresh -> {
                _state.update {
                    it.copy(
                        isRefreshing = true,
                    )
                }
                loadData()
            }

            is SearchAction.OnLoadMore -> {
                loadData(_state.value.lastDoc)
            }

            is SearchAction.OnItemClick -> {

            }
        }
    }
}