package com.kafasan.store.domain.local;

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.appDataStore by preferencesDataStore("kafasan_prefs")

class AppPreferences(private val context:Context) {
    private object PreferencesKey {
        val REMEMBER_ME = booleanPreferencesKey("remember_me")
    }

    val isRememberMe: Flow<Boolean> = context.appDataStore.data.map { prefs ->
        prefs[PreferencesKey.REMEMBER_ME] ?: false
    }

    suspend fun toggleRememberMe(isRemember: Boolean) {
        context.appDataStore.edit { prefs ->
            prefs[PreferencesKey.REMEMBER_ME] = isRemember
        }
    }
}
