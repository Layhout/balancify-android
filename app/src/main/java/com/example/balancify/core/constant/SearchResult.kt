package com.example.balancify.core.constant

import android.os.Parcelable
import com.example.balancify.domain.model.FriendModel
import com.example.balancify.domain.model.GroupModel
import kotlinx.parcelize.Parcelize

sealed interface SearchResult {
    @Parcelize
    data class Friend(val data: FriendModel) : SearchResult, Parcelable

    @Parcelize
    data class Group(val data: GroupModel) : SearchResult, Parcelable
}
