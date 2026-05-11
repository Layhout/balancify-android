package com.macrobytes.balancify.data.data_source.friend

import com.macrobytes.balancify.core.constant.FirebaseCollectionName
import com.macrobytes.balancify.core.constant.ITEMS_LIMIT
import com.macrobytes.balancify.core.ext.getTrigram
import com.macrobytes.balancify.domain.model.FriendModel
import com.macrobytes.balancify.domain.model.FriendStatus
import com.macrobytes.balancify.service.AuthService
import com.macrobytes.balancify.service.BatchDeleteItem
import com.macrobytes.balancify.service.BatchSetItem
import com.macrobytes.balancify.service.BatchUpdateItem
import com.macrobytes.balancify.service.DatabaseService
import com.macrobytes.balancify.service.PaginatedData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject

class FriendRemoteDataSourceImp(
    private val db: DatabaseService,
    private val auth: AuthService,
) : FriendRemoteDataSource {
    private fun buildCollectionPath(id: String = auth.userId): String {
        return "${FirebaseCollectionName.FRIENDS.value}/${id}/data"
    }

    override suspend fun getFriends(
        lastDoc: DocumentSnapshot?,
        search: String?
    ): PaginatedData<FriendModel> {
        val result = db.getPage(
            buildCollectionPath(), ITEMS_LIMIT, lastDoc,
            queryBuilder = {
                var query = it

                if (!search.isNullOrBlank()) {
                    query = query.whereArrayContainsAny(
                        "nameTrigrams",
                        search.getTrigram()
                    )
                }

                query.whereNotEqualTo("status", FriendStatus.REJECTED)
                    .orderBy("createdAt", Query.Direction.DESCENDING)
            }
        )

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
                    data = friend,
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