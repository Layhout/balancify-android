package com.example.balancify.presentation.search

import com.example.balancify.core.constant.SearchType

sealed interface SearchAction {
    data class OnSearchTypeReceive(val type: SearchType) : SearchAction
    data class OnSearchClick(val searchTerm: String) : SearchAction
    data object OnRefresh : SearchAction
    data object OnLoadMore : SearchAction
}