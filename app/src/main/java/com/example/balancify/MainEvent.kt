package com.example.balancify

import com.example.balancify.core.constant.SearchResult

sealed interface MainEvent {
    data class OnSearchResult(val result: SearchResult) : MainEvent
}