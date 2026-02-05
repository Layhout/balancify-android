package com.example.balancify.domain.repository

import com.example.balancify.core.constant.RepositoryResult
import com.example.balancify.domain.model.FriendModel
import com.example.balancify.domain.model.PaginatedFriendsModel
import com.google.firebase.firestore.DocumentSnapshot

interface FriendRepository {
    suspend fun getFriends(lastDoc: DocumentSnapshot?): RepositoryResult<PaginatedFriendsModel>
    suspend fun unfriend(id: String): RepositoryResult<Unit>
    suspend fun acceptFriend(id: String): RepositoryResult<Unit>
    suspend fun rejectFriend(id: String): RepositoryResult<Unit>
    suspend fun addFriend(friend: FriendModel, youAsFriend: FriendModel): RepositoryResult<Unit>
    suspend fun getFriend(id: String): RepositoryResult<FriendModel?>
}