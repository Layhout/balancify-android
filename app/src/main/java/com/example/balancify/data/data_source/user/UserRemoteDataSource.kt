package com.example.balancify.data.data_source.user

import com.example.balancify.domain.model.UserModel

interface UserRemoteDataSource {
    suspend fun getUser(id: String): UserModel?
    suspend fun getUserByEmail(id: String): UserModel?
    suspend fun addUser(user: UserModel)
    suspend fun getUserByIds(ids: List<String>): List<UserModel>
}