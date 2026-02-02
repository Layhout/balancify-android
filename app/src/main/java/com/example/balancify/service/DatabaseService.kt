package com.example.balancify.service

import com.example.balancify.BuildConfig
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
        return if (BuildConfig.DEBUG) "test/dev/${collection}" else collection
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
        build: Query.() -> Query = { this }
    ): PageResult {
        var query = collectionRef(collection).build().limit(pageSize + 1)
        if (lastDoc != null) query = query.startAfter(lastDoc)


        return PageResult(
            snapshot = query.get().await(),
            canLoadMore = query.get().await().size() > pageSize + 1
        )
    }

    suspend fun batchSet(
        collection: String,
        items: List<Pair<String, Any>>,
        merge: Boolean = true
    ) {
        if (items.isEmpty()) return

        db.runBatch { batch ->
            items.forEach { (id, data) ->
                val ref = documentRef(collection, id)
                if (merge) batch.set(ref, data, SetOptions.merge())
                else batch.set(ref, data)
            }
        }.await()
    }

    suspend fun batchUpdate(
        collection: String,
        items: List<Pair<String, Map<String, Any?>>>
    ) {
        if (items.isEmpty()) return

        db.runBatch { batch ->
            items.forEach { (id, fields) ->
                val ref = documentRef(collection, id)
                batch.update(ref, fields)
            }
        }.await()
    }

    suspend fun batchDelete(
        collection: String,
        ids: List<String>
    ) {
        if (ids.isEmpty()) return

        db.runBatch { batch ->
            ids.forEach { id ->
                batch.delete(documentRef(collection, id))
            }
        }.await()
    }
}
