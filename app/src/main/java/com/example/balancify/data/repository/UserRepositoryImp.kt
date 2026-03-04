package com.example.balancify.data.repository

import com.example.balancify.data.data_source.user.UserLocalDataSource
import com.example.balancify.data.data_source.user.UserRemoteDataSource
import com.example.balancify.domain.model.UserModel
import com.example.balancify.domain.repository.UserRepository

class UserRepositoryImp(
    private val remoteDataSource: UserRemoteDataSource,
    private val localDataSource: UserLocalDataSource
) : UserRepository {
    override suspend fun getUser(id: String): Result<UserModel?> {
        return Result.runCatching {
            remoteDataSource.getUser(id)
        }
    }

    override suspend fun addUser(user: UserModel): Result<Unit> {
        return Result.runCatching {
            remoteDataSource.addUser(user)
        }
    }

    override suspend fun getLocalUser(): Result<UserModel?> {
        return Result.runCatching {
            localDataSource.getUser()
        }
    }

    override suspend fun addLocalUser(user: UserModel): Result<Unit> {
        return Result.runCatching {
            localDataSource.addUser(user)
        }
    }

    override suspend fun getUserByIds(ids: List<String>): Result<List<UserModel>> {
        return Result.runCatching {
            remoteDataSource.getUserByIds(ids)
        }
    }

    override suspend fun getUserByEmail(email: String): Result<UserModel?> {
        return Result.runCatching {
            remoteDataSource.getUserByEmail(email)
        }
    }
}