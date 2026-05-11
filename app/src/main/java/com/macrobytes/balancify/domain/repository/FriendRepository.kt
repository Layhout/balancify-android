package com.macrobytes.balancify.domain.repository

import com.macrobytes.balancify.domain.model.FriendModel
import com.macrobytes.balancify.service.PaginatedData
import com.google.firebase.firestore.DocumentSnapshot

interface FriendRepository {
    suspend fun getFriends(
        lastDoc: DocumentSnapshot?,
        search: String? = null,
    ): Result<PaginatedData<FriendModel>>

    suspend fun unfriend(id: String): Result<Unit>
    suspend fun acceptFriend(id: String): Result<Unit>
    suspend fun rejectFriend(id: String): Result<Unit>
    suspend fun addFriend(friend: FriendModel, youAsFriend: FriendModel): Result<Unit>
    suspend fun getFriend(id: String): Result<FriendModel?>
}