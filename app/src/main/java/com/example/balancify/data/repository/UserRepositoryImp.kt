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
        return try {
            val result = remoteDataSource.getUser(id)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addUser(user: UserModel): Result<Unit> {
        return try {
            remoteDataSource.addUser(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getLocalUser(): Result<UserModel?> {
        return try {
            val result = localDataSource.getUser()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addLocalUser(user: UserModel): Result<Unit> {
        return try {
            localDataSource.addUser(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserByIds(ids: List<String>): Result<List<UserModel>> {
        return try {
            val result = remoteDataSource.getUserByIds(ids)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserByEmail(email: String): Result<UserModel?> {
        return try {
            val result = remoteDataSource.getUserByEmail(email)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}