package com.example.balancify.domain.repository

import com.example.balancify.core.RepositoryResult
import com.example.balancify.domain.model.UserModel

interface UserRepository {
    suspend fun getUser(id: String): RepositoryResult<UserModel?>
    suspend fun addUser(user: UserModel): RepositoryResult<Unit>
}
