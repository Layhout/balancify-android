package com.example.balancify.core.constant

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey

data class LocalDataStoreKey<T>(
    val key: Preferences.Key<T>,
    val defaultValue: T
) {
    companion object {
        val USER = LocalDataStoreKey(stringPreferencesKey("user"), "")
    }
}
