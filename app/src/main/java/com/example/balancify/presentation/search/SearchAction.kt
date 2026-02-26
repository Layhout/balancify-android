package com.example.balancify.presentation.search

sealed interface SearchAction {
    data class OnSearchClick(val searchTerm: String) : SearchAction
    data object OnRefresh : SearchAction
    data object OnLoadMore : SearchAction
    data class OnItemClick(val id: String) : SearchAction
}