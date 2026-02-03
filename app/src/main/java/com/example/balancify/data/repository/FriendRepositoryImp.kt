package com.example.balancify.data.repository

import com.example.balancify.core.constant.RepositoryResult
import com.example.balancify.data.data_source.friend.FriendRemoteDataSource
import com.example.balancify.domain.model.FriendModel
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

    override suspend fun unfriend(id: String): RepositoryResult<Unit> {
        try {
            remoteDataSource.unfriend(id)
            return RepositoryResult.Success(Unit)
        } catch (e: Exception) {
            return RepositoryResult.Error(e)
        }
    }

    override suspend fun acceptFriend(id: String): RepositoryResult<Unit> {
        try {
            remoteDataSource.acceptFriend(id)
            return RepositoryResult.Success(Unit)
        } catch (e: Exception) {
            return RepositoryResult.Error(e)
        }
    }

    override suspend fun rejectFriend(id: String): RepositoryResult<Unit> {
        try {
            remoteDataSource.rejectFriend(id)
            return RepositoryResult.Success(Unit)
        } catch (e: Exception) {
            return RepositoryResult.Error(e)
        }
    }

    override suspend fun addFriend(
        friend: FriendModel,
        youAsFriend: FriendModel
    ): RepositoryResult<Unit> {
        try {
            remoteDataSource.addFriend(friend, youAsFriend)
            return RepositoryResult.Success(Unit)
        } catch (e: Exception) {
            return RepositoryResult.Error(e)
        }
    }

    override suspend fun getFriend(id: String): RepositoryResult<FriendModel?> {
        try {
            val result = remoteDataSource.getFriend(id)
            return RepositoryResult.Success(result)
        } catch (e: Exception) {
            return RepositoryResult.Error(e)
        }
    }
}