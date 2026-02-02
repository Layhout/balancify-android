package com.example.balancify.data.data_source.friend

import com.example.balancify.domain.model.FriendModel
import com.example.balancify.domain.model.PaginatedFriendsModel
import com.example.balancify.service.DatabaseService
import com.google.firebase.firestore.DocumentSnapshot

class FriendRemoteDataSourceImp(private val db: DatabaseService) : FriendRemoteDataSource {

    private val collectionName: String = "friends"


    override suspend fun getFriends(lastDoc: DocumentSnapshot?): PaginatedFriendsModel {
        val result = db.getPage(collectionName, 20, lastDoc)

        val friends = result.snapshot.documents.mapNotNull {
            it.toObject(FriendModel::class.java)
        }

        val canLoadMore = result.canLoadMore

        return PaginatedFriendsModel(
            friends = friends,
            canLoadMore = canLoadMore
        )
    }
}