package com.example.balancify.data.repository

import com.example.balancify.core.constant.RepositoryResult
import com.example.balancify.data.data_source.user.UserLocalDataSource
import com.example.balancify.data.data_source.user.UserRemoteDataSource
import com.example.balancify.domain.model.UserModel
import com.example.balancify.domain.repository.UserRepository

class UserRepositoryImp(
    private val remoteDataSource: UserRemoteDataSource,
    private val localDataSource: UserLocalDataSource
) : UserRepository {
    override suspend fun getUser(id: String): RepositoryResult<UserModel?> {
        return try {
            val result = remoteDataSource.getUser(id)
            RepositoryResult.Success(result)
        } catch (e: Exception) {
            RepositoryResult.Error(e)
        }
    }

    override suspend fun addUser(user: UserModel): RepositoryResult<Unit> {
        return try {
            remoteDataSource.addUser(user)
            RepositoryResult.Success(Unit)
        } catch (e: Exception) {
            RepositoryResult.Error(e)
        }
    }

    override suspend fun getLocalUser(): RepositoryResult<UserModel?> {
        return try {
            val result = localDataSource.getUser()
            RepositoryResult.Success(result)
        } catch (e: Exception) {
            RepositoryResult.Error(e)
        }
    }

    override suspend fun addLocalUser(user: UserModel): RepositoryResult<Unit> {
        return try {
            localDataSource.addUser(user)
            RepositoryResult.Success(Unit)
        } catch (e: Exception) {
            RepositoryResult.Error(e)
        }
    }

    override suspend fun getUserByIds(ids: List<String>): RepositoryResult<List<UserModel>> {
        return try {
            val result = remoteDataSource.getUserByIds(ids)
            RepositoryResult.Success(result)
        } catch (e: Exception) {
            RepositoryResult.Error(e)
        }
    }

    override suspend fun getUserByEmail(email: String): RepositoryResult<UserModel?> {
        return try {
            val result = remoteDataSource.getUserByEmail(email)
            RepositoryResult.Success(result)
        } catch (e: Exception) {
            RepositoryResult.Error(e)
        }
    }
}