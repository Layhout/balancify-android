package com.example.balancify.data.repository

import com.example.balancify.data.data_source.friend.FriendRemoteDataSource
import com.example.balancify.domain.model.FriendModel
import com.example.balancify.domain.repository.FriendRepository
import com.example.balancify.service.PaginatedData
import com.google.firebase.firestore.DocumentSnapshot

class FriendRepositoryImp(
    private val remoteDataSource: FriendRemoteDataSource
) : FriendRepository {
    override suspend fun getFriends(
        lastDoc: DocumentSnapshot?,
        search: String?,
    ): Result<PaginatedData<FriendModel>> {
        return Result.runCatching {
            remoteDataSource.getFriends(lastDoc, search)
        }
    }

    override suspend fun unfriend(id: String): Result<Unit> {
        return Result.runCatching {
            remoteDataSource.unfriend(id)
        }
    }

    override suspend fun acceptFriend(id: String): Result<Unit> {
        return Result.runCatching {
            remoteDataSource.acceptFriend(id)
        }
    }

    override suspend fun rejectFriend(id: String): Result<Unit> {
        return Result.runCatching {
            remoteDataSource.rejectFriend(id)
        }
    }

    override suspend fun addFriend(
        friend: FriendModel,
        youAsFriend: FriendModel
    ): Result<Unit> {
        return Result.runCatching {
            remoteDataSource.addFriend(friend, youAsFriend)
        }
    }

    override suspend fun getFriend(id: String): Result<FriendModel?> {
        return Result.runCatching {
            remoteDataSource.getFriend(id)
        }
    }
}