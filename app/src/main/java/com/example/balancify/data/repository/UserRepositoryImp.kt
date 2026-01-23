package com.example.balancify.data.repository

import com.example.balancify.core.RepositoryResult
import com.example.balancify.data.data_source.user.UserRemoteDataSource
import com.example.balancify.domain.model.UserModel
import com.example.balancify.domain.repository.UserRepository

class UserRepositoryImp(private val remoteDataSource: UserRemoteDataSource) : UserRepository {
    override suspend fun getUser(id: String): RepositoryResult<UserModel?> {
        try {
            val result = remoteDataSource.getUser(id)
            return RepositoryResult.Success(result)
        } catch (e: Exception) {
            return RepositoryResult.Error(e)
        }
    }

    override suspend fun addUser(user: UserModel): RepositoryResult<Unit> {
        try {
            remoteDataSource.addUser(user)
            return RepositoryResult.Success(Unit)
        } catch (e: Exception) {
            return RepositoryResult.Error(e)
        }
    }
}