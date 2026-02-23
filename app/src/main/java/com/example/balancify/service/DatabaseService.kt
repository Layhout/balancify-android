package com.example.balancify.service

import com.example.balancify.core.constant.BatchDeleteItem
import com.example.balancify.core.constant.BatchSetItem
import com.example.balancify.core.constant.BatchUpdateItem
import com.example.balancify.core.constant.PageResult
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await


class DatabaseService {
    private val db = Firebase.firestore

    private fun buildCollectionPath(collection: String): String {
        return collection
//        return if (BuildConfig.DEBUG) "test/dev/${collection}" else collection
    }

    private fun collectionRef(collection: String): CollectionReference {
        return db.collection(buildCollectionPath(collection))
    }

    private fun documentRef(collection: String, id: String): DocumentReference {
        return collectionRef(collection).document(id)
    }

    suspend fun setData(collection: String, id: String, data: Any) {
        documentRef(collection, id)
            .set(data, SetOptions.merge())
            .await()
    }

    suspend fun deleteData(collection: String, id: String) {
        documentRef(collection, id)
            .delete()
            .await()
    }

    suspend fun getData(collection: String, id: String): DocumentSnapshot {
        return documentRef(collection, id)
            .get()
            .await()
    }

    suspend fun updateData(collection: String, id: String, data: Map<String, Any?>) {
        documentRef(collection, id)
            .update(data)
            .await()
    }

    suspend fun exists(collection: String, id: String): Boolean {
        return getData(collection, id).exists()
    }

    suspend fun getDataWithQuery(
        collection: String,
        build: Query.() -> Query
    ): QuerySnapshot {
        val query = collectionRef(collection).build()
        return query.get().await()
    }

    suspend fun getDocumentsWithQuery(
        collection: String,
        build: Query.() -> Query
    ): List<DocumentSnapshot> {
        return getDataWithQuery(collection, build).documents
    }

    suspend fun getPage(
        collection: String,
        pageSize: Long,
        lastDoc: DocumentSnapshot? = null,
        queryBuilder: (Query) -> Query = { it }
    ): PageResult {
        var query = queryBuilder(
            collectionRef(collection).limit(pageSize + 1)
        )

        lastDoc?.let {
            query = query.startAfter(it)
        }

        val snapshot = query.get().await()

        val canLoadMore = snapshot.size() > pageSize

        return PageResult(
            snapshot = snapshot,
            canLoadMore = canLoadMore
        )
    }

    suspend fun batchSet(
        items: List<BatchSetItem>
    ) {
        if (items.isEmpty()) return

        db.runBatch { batch ->
            items.forEach { item ->
                val ref = documentRef(item.collection, item.id)
                if (item.merge) batch.set(ref, item.data, SetOptions.merge())
                else batch.set(ref, item.data)
            }
        }.await()
    }

    suspend fun batchUpdate(
        items: List<BatchUpdateItem>
    ) {
        if (items.isEmpty()) return

        db.runBatch { batch ->
            items.forEach { item ->
                val ref = documentRef(item.collection, item.id)
                batch.update(ref, item.fields)
            }
        }.await()
    }

    suspend fun batchDelete(
        items: List<BatchDeleteItem>
    ) {
        if (items.isEmpty()) return

        db.runBatch { batch ->
            items.forEach { item ->
                batch.delete(documentRef(item.collection, item.id))
            }
        }.await()
    }
}
