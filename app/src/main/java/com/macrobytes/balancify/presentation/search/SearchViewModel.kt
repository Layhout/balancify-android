package com.macrobytes.balancify.presentation.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.macrobytes.balancify.core.constant.SearchResult
import com.macrobytes.balancify.core.constant.SearchType
import com.macrobytes.balancify.core.manager.GlobalAppStateManager
import com.macrobytes.balancify.domain.model.FoundItemData
import com.macrobytes.balancify.domain.use_case.search.SearchUseCases
import com.macrobytes.balancify.navigatin.Routes
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchUseCases: SearchUseCases,
    private val globalAppStateManager: GlobalAppStateManager,
    private val handle: SavedStateHandle,
) : ViewModel() {
    private val _state = MutableStateFlow(SearchState())

    val state = _state.onStart {
        val searchArg = handle.toRoute<Routes.Search>()
        _state.value = _state.value.copy(
            searchType = searchArg.type
        )
    }.stateIn(
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

    private fun loadData(lastDoc: DocumentSnapshot? = null, isLoading: Boolean = true) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = isLoading) }
            val result =
                if (_state.value.searchType == SearchType.FRIEND) searchUseCases.findFriends(
                    lastDoc,
                    _state.value.searchTerm
                )
                else searchUseCases.findGroups(lastDoc, _state.value.searchTerm)


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
                loadData(isLoading = true)
            }

            is SearchAction.OnRefresh -> {
                _state.update {
                    it.copy(
                        isRefreshing = true,
                    )
                }
                loadData(isLoading = false)
            }

            is SearchAction.OnLoadMore -> {
                loadData(_state.value.lastDoc)
            }

            is SearchAction.OnItemClick -> {
                val result = _state.value.foundItems.find { it.id == action.id }

                if (result != null) {
                    globalAppStateManager.setSearchResult(
                        if (_state.value.searchType == SearchType.FRIEND)
                            SearchResult.Friend((result.data as FoundItemData.Friend).data)
                        else
                            SearchResult.Group((result.data as FoundItemData.Group).data)
                    )
                }
            }
        }
    }
}