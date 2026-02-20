package com.example.balancify

import com.example.balancify.core.constant.SearchResult

sealed interface MainAction {
    data class OnEmitSearchResult(val result: SearchResult) : MainAction
}