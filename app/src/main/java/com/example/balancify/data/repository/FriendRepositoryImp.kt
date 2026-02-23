package com.example.balancify.data.repository

import com.example.balancify.core.constant.PaginatedData
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
    ): Result<PaginatedData<FriendModel>> {
        return try {
            val result = remoteDataSource.getFriends(lastDoc, search)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun unfriend(id: String): Result<Unit> {
        return try {
            remoteDataSource.unfriend(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun acceptFriend(id: String): Result<Unit> {
        return try {
            remoteDataSource.acceptFriend(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun rejectFriend(id: String): Result<Unit> {
        return try {
            remoteDataSource.rejectFriend(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addFriend(
        friend: FriendModel,
        youAsFriend: FriendModel
    ): Result<Unit> {
        return try {
            remoteDataSource.addFriend(friend, youAsFriend)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFriend(id: String): Result<FriendModel?> {
        return try {
            val result = remoteDataSource.getFriend(id)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}