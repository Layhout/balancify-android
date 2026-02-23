package com.example.balancify.domain.repository

import com.example.balancify.domain.model.UserModel

interface UserRepository {
    suspend fun getUser(id: String): Result<UserModel?>
    suspend fun getUserByEmail(email: String): Result<UserModel?>
    suspend fun addUser(user: UserModel): Result<Unit>
    suspend fun getLocalUser(): Result<UserModel?>
    suspend fun addLocalUser(user: UserModel): Result<Unit>
    suspend fun getUserByIds(ids: List<String>): Result<List<UserModel>>
}
