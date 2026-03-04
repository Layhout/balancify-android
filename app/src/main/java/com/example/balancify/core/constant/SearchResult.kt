package com.example.balancify.core.constant

import android.os.Parcelable
import com.example.balancify.domain.model.FriendModel
import kotlinx.parcelize.Parcelize

sealed interface SearchResult {
    @Parcelize
    data class Friend(val data: FriendModel) : SearchResult, Parcelable

    data class Group(val data: Any) : SearchResult
}
