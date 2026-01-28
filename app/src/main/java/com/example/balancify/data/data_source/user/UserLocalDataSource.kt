package com.example.balancify.data.data_source.user

import com.example.balancify.domain.model.UserModel

interface UserLocalDataSource {
    suspend fun getUser(): UserModel?
    suspend fun addUser(user: UserModel)
}