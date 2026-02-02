package com.example.balancify.data.repository

import com.example.balancify.core.constant.RepositoryResult
import com.example.balancify.data.data_source.friend.FriendRemoteDataSource
import com.example.balancify.domain.model.PaginatedFriendsModel
import com.example.balancify.domain.repository.FriendRepository
import com.google.firebase.firestore.DocumentSnapshot

class FriendRepositoryImp(
    private val remoteDataSource: FriendRemoteDataSource
) : FriendRepository {
    override suspend fun getFriends(lastDoc: DocumentSnapshot?): RepositoryResult<PaginatedFriendsModel> {
        try {
            val result = remoteDataSource.getFriends(lastDoc)
            return RepositoryResult.Success(result)
        } catch (e: Exception) {
            return RepositoryResult.Error(e)
        }
    }
}