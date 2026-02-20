package com.example.balancify.data.data_source.friend

import com.example.balancify.core.constant.BatchDeleteItem
import com.example.balancify.core.constant.BatchSetItem
import com.example.balancify.core.constant.BatchUpdateItem
import com.example.balancify.core.constant.FriendStatus
import com.example.balancify.core.constant.ITEMS_LIMIT
import com.example.balancify.core.constant.PaginatedData
import com.example.balancify.core.ext.getTrigram
import com.example.balancify.domain.model.FriendModel
import com.example.balancify.service.AuthService
import com.example.balancify.service.DatabaseService
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.toObject

class FriendRemoteDataSourceImp(
    private val db: DatabaseService,
    private val auth: AuthService
) : FriendRemoteDataSource {
    private fun buildCollectionPath(id: String = auth.userId): String {
        return "friends/${id}/data"
    }

    override suspend fun getFriends(
        lastDoc: DocumentSnapshot?,
        search: String?
    ): PaginatedData<FriendModel> {
        val result = db.getPage(buildCollectionPath(), ITEMS_LIMIT, lastDoc, build = {
            if (!search.isNullOrBlank()) whereArrayContainsAny(
                "nameTrigrams",
                search.getTrigram()
            )
            whereNotEqualTo("status", FriendStatus.REJECTED)
        })

        val friends = result.snapshot.documents.mapNotNull {
            it.toObject<FriendModel>()
        }

        val canLoadMore = result.canLoadMore

        return PaginatedData(
            data = friends,
            canLoadMore = canLoadMore,
            lastDoc = result.snapshot.documents.lastOrNull()
        )
    }

    override suspend fun unfriend(id: String) {
        db.batchDelete(
            listOf(
                BatchDeleteItem(
                    collection = buildCollectionPath(),
                    id = id
                ),
                BatchDeleteItem(
                    collection = buildCollectionPath(id),
                    id = auth.userId
                )
            )
        )
    }

    override suspend fun acceptFriend(id: String) {
        db.batchUpdate(
            listOf(
                BatchUpdateItem(
                    collection = buildCollectionPath(),
                    id = id,
                    fields = mapOf(
                        "status" to FriendStatus.ACCEPTED
                    )
                ),
                BatchUpdateItem(
                    collection = buildCollectionPath(id),
                    id = auth.userId,
                    fields = mapOf(
                        "status" to FriendStatus.ACCEPTED
                    )
                )
            )
        )
    }

    override suspend fun rejectFriend(id: String) {
        db.batchUpdate(
            listOf(
                BatchUpdateItem(
                    collection = buildCollectionPath(),
                    id = id,
                    fields = mapOf(
                        "status" to FriendStatus.REJECTED
                    )
                ),
                BatchUpdateItem(
                    collection = buildCollectionPath(id),
                    id = auth.userId,
                    fields = mapOf(
                        "status" to FriendStatus.REJECTED
                    )
                )
            )
        )
    }

    override suspend fun addFriend(friend: FriendModel, youAsFriend: FriendModel) {
        db.batchSet(
            listOf(
                BatchSetItem(
                    collection = buildCollectionPath(),
                    id = friend.userId,
                    data = friend
                ),
                BatchSetItem(
                    collection = buildCollectionPath(friend.userId),
                    id = auth.userId,
                    data = youAsFriend,
                )
            )
        )
    }

    override suspend fun getFriend(id: String): FriendModel? {
        return db.getData(buildCollectionPath(), id).toObject<FriendModel>()
    }
}