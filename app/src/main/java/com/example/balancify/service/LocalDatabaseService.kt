package com.example.balancify.service

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.balancify.core.constant.LocalDataStoreKey
import kotlinx.coroutines.flow.first

val Context.datastore by preferencesDataStore(name = "balancify")


class LocalDatabaseService(private val context: Context) {
    suspend fun <T> setData(localDataStoreKey: LocalDataStoreKey<T>, value: T) {
        context.datastore.edit {
            it[localDataStoreKey.key] = value
        }

    }

    suspend fun <T> getData(localDataStoreKey: LocalDataStoreKey<T>): T {
        return context.datastore.data.first()[localDataStoreKey.key]
            ?: localDataStoreKey.defaultValue
    }
}

