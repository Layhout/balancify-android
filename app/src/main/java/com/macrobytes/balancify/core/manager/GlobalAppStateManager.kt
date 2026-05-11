package com.macrobytes.balancify.core.manager

import com.macrobytes.balancify.core.constant.GlobalAppStateFlag
import com.macrobytes.balancify.core.constant.SearchResult

class GlobalAppStateManager {
    private var searchResult: SearchResult? = null
    private val boolFlags = mutableMapOf<GlobalAppStateFlag, Boolean>()

    fun setSearchResult(result: SearchResult) {
        searchResult = result
    }

    fun getSearchResult(): SearchResult? {
        val result = searchResult
        searchResult = null
        return result
    }

    fun setFlag(key: GlobalAppStateFlag, value: Boolean) {
        boolFlags[key] = value
    }

    fun getFlag(key: GlobalAppStateFlag): Boolean {
        return boolFlags[key] ?: false
    }

    fun pullFlag(key: GlobalAppStateFlag): Boolean {
        return boolFlags.remove(key) ?: false
    }
}