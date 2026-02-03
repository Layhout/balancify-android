package com.example.balancify.data.data_source.friend

import com.example.balancify.domain.model.FriendModel
import com.example.balancify.domain.model.PaginatedFriendsModel
import com.example.balancify.service.AuthService
import com.example.balancify.service.DatabaseService
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.toObject

class FriendRemoteDataSourceImp(
    private val db: DatabaseService,
    private val auth: AuthService
) : FriendRemoteDataSource {
    private val collectionName: String = "friends"


    override suspend fun getFriends(lastDoc: DocumentSnapshot?): PaginatedFriendsModel {
        val result = db.getPage("${collectionName}/${auth.userId}/data", 20, lastDoc)

        val size = result.snapshot.documents.size

        println(size)

        val friends = result.snapshot.documents.mapNotNull {
            it.toObject<FriendModel>()
        }

        val canLoadMore = result.canLoadMore

        return PaginatedFriendsModel(
            friends = friends,
            canLoadMore = canLoadMore
        )
    }
}