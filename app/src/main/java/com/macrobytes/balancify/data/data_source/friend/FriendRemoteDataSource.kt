package com.macrobytes.balancify.data.data_source.friend

import com.macrobytes.balancify.domain.model.FriendModel
import com.macrobytes.balancify.service.PaginatedData
import com.google.firebase.firestore.DocumentSnapshot

interface FriendRemoteDataSource {
    suspend fun getFriends(
        lastDoc: DocumentSnapshot?,
        search: String?,
    ): PaginatedData<FriendModel>

    suspend fun unfriend(id: String)
    suspend fun acceptFriend(id: String)
    suspend fun rejectFriend(id: String)
    suspend fun addFriend(friend: FriendModel, youAsFriend: FriendModel)
    suspend fun getFriend(id: String): FriendModel?
}