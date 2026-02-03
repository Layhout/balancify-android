package com.example.balancify.data.data_source.user

import com.example.balancify.domain.model.UserModel
import com.example.balancify.service.DatabaseService
import com.google.firebase.firestore.FieldPath.documentId
import com.google.firebase.firestore.toObject

class UserRemoteDataSourceImp(private val db: DatabaseService) : UserRemoteDataSource {
    private val collectionName: String = "users"

    override suspend fun getUser(id: String): UserModel? {
        return db.getData(collectionName, id).toObject<UserModel>()
    }

    override suspend fun addUser(user: UserModel) {
        db.setData(collectionName, user.documentId, user)
    }

    override suspend fun getUserByIds(ids: List<String>): List<UserModel> {
        return db.getDocumentsWithQuery(collectionName, build = {
            whereIn(documentId(), ids)
        }).mapNotNull { it.toObject<UserModel>() }
    }

    override suspend fun getUserByEmail(id: String): UserModel? {
        return db.getDataWithQuery(collectionName, build = {
            whereEqualTo("email", id)
        }).documents.firstOrNull()?.toObject<UserModel>()
    }
}
