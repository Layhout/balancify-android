package com.example.balancify.domain.repository

import com.example.balancify.core.constant.RepositoryResult
import com.example.balancify.domain.model.UserModel

interface UserRepository {
    suspend fun getUser(id: String): RepositoryResult<UserModel?>
    suspend fun addUser(user: UserModel): RepositoryResult<Unit>
    suspend fun getLocalUser(): RepositoryResult<UserModel?>
    suspend fun addLocalUser(user: UserModel): RepositoryResult<Unit>
    suspend fun getUserByIds(ids: List<String>): RepositoryResult<List<UserModel>>
}
