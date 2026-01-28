package com.example.balancify.data.data_source.user

import com.example.balancify.core.LocalDataStoreKey
import com.example.balancify.domain.model.UserModel
import com.example.balancify.service.LocalDatabaseService
import kotlinx.serialization.json.Json

class UserLocalDataSourceImp(private val db: LocalDatabaseService) : UserLocalDataSource {
    override suspend fun getUser(): UserModel? {
        val jsonString = db.getData(LocalDataStoreKey.USER)

        if (jsonString.isEmpty()) {
            return null
        }

        return Json.decodeFromString(jsonString)
    }

    override suspend fun addUser(user: UserModel) {
        db.setData(LocalDataStoreKey.USER, Json.encodeToString(user))
    }
}
