package com.example.balancify.data.data_source.friend

import com.example.balancify.domain.model.PaginatedFriendsModel
import com.google.firebase.firestore.DocumentSnapshot

interface FriendRemoteDataSource {
    suspend fun getFriends(lastDoc: DocumentSnapshot?): PaginatedFriendsModel
}