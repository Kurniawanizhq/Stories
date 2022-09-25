package com.eone.submission2_bpai

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SessionPreference @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private val _token = stringPreferencesKey("token")

    fun getRealtimeToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[_token] ?: ""
        }
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[_token] = token
        }
    }

    suspend fun removeToken() {
        dataStore.edit { preferences ->
            preferences.remove(_token)
        }
    }
}
