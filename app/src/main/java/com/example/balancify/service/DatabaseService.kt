package com.example.balancify.service

import com.example.balancify.BuildConfig
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class DatabaseService {
    private val db = Firebase.firestore

    private fun buildCollectionPath(collection: String): String {
        return if (BuildConfig.DEBUG) "test/dev/${collection}" else collection
    }

    suspend fun setData(collection: String, id: String, data: Any) {
        db.collection(buildCollectionPath(collection))
            .document(id)
            .set(data, SetOptions.merge())
            .await()
    }

    suspend fun deleteData(collection: String, id: String) {
        db.collection(buildCollectionPath(collection))
            .document(id)
            .delete()
            .await()
    }

    suspend fun getData(collection: String, id: String): DocumentSnapshot {
        return db.collection(buildCollectionPath(collection))
            .document(id)
            .get()
            .await()
    }
}
