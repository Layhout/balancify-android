package com.example.balancify.domain.repository

import com.example.balancify.core.constant.RepositoryResult
import com.example.balancify.domain.model.PaginatedFriendsModel
import com.google.firebase.firestore.DocumentSnapshot

interface FriendRepository {
    suspend fun getFriends(lastDoc: DocumentSnapshot?): RepositoryResult<PaginatedFriendsModel>
}