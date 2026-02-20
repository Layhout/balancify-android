package com.example.balancify.core.constant

import com.example.balancify.domain.model.FriendModel

sealed interface SearchResult {
    data class Friend(val data: FriendModel?) : SearchResult
    data class Group(val data: Any) : SearchResult
}
