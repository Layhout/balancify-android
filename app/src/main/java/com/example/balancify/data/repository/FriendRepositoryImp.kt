package com.example.balancify.data.repository

import com.example.balancify.core.constant.PaginatedData
import com.example.balancify.core.constant.RepositoryResult
import com.example.balancify.data.data_source.friend.FriendRemoteDataSource
import com.example.balancify.domain.model.FriendModel
import com.example.balancify.domain.repository.FriendRepository
import com.google.firebase.firestore.DocumentSnapshot

class FriendRepositoryImp(
    private val remoteDataSource: FriendRemoteDataSource
) : FriendRepository {
    override suspend fun getFriends(
        lastDoc: DocumentSnapshot?,
        search: String?,
    ): RepositoryResult<PaginatedData<FriendModel>> {
        return try {
            val result = remoteDataSource.getFriends(lastDoc, search)
            RepositoryResult.Success(result)
        } catch (e: Exception) {
            RepositoryResult.Error(e)
        }
    }

    override suspend fun unfriend(id: String): RepositoryResult<Unit> {
        return try {
            remoteDataSource.unfriend(id)
            RepositoryResult.Success(Unit)
        } catch (e: Exception) {
            RepositoryResult.Error(e)
        }
    }

    override suspend fun acceptFriend(id: String): RepositoryResult<Unit> {
        return try {
            remoteDataSource.acceptFriend(id)
            RepositoryResult.Success(Unit)
        } catch (e: Exception) {
            RepositoryResult.Error(e)
        }
    }

    override suspend fun rejectFriend(id: String): RepositoryResult<Unit> {
        return try {
            remoteDataSource.rejectFriend(id)
            RepositoryResult.Success(Unit)
        } catch (e: Exception) {
            RepositoryResult.Error(e)
        }
    }

    override suspend fun addFriend(
        friend: FriendModel,
        youAsFriend: FriendModel
    ): RepositoryResult<Unit> {
        return try {
            remoteDataSource.addFriend(friend, youAsFriend)
            RepositoryResult.Success(Unit)
        } catch (e: Exception) {
            RepositoryResult.Error(e)
        }
    }

    override suspend fun getFriend(id: String): RepositoryResult<FriendModel?> {
        return try {
            val result = remoteDataSource.getFriend(id)
            RepositoryResult.Success(result)
        } catch (e: Exception) {
            RepositoryResult.Error(e)
        }
    }
}