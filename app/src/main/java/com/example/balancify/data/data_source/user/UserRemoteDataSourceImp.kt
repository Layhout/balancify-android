package com.example.balancify.data.data_source.user

import com.example.balancify.domain.model.UserModel
import com.example.balancify.service.DatabaseService
import com.google.firebase.firestore.toObject

class UserRemoteDataSourceImp(private val db: DatabaseService) : UserRemoteDataSource {
    private val collectionName: String = "users"

    override suspend fun getUser(id: String): UserModel? {
        return db.getData(collectionName, id).toObject<UserModel>()
    }

    override suspend fun addUser(user: UserModel) {
        db.setData(collectionName, user.documentId, user)
    }
}
