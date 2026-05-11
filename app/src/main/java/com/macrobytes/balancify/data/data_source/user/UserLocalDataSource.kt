package com.macrobytes.balancify.data.data_source.user

import com.macrobytes.balancify.domain.model.UserModel

interface UserLocalDataSource {
    suspend fun getUser(): UserModel?
    suspend fun addUser(user: UserModel)
}