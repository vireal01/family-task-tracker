package com.vireal.familytasktracker.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.IOException
import javax.inject.Inject

data class UserPreferences(
    val showCompletedTasks: Boolean,
)

class UserPreferencesRepository @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private val tag: String = "UserPreferencesRepo"

    private object PreferencesKeys {
        val SHOW_COMPLETED_TASKS = booleanPreferencesKey("show_completed_tasks")
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e(tag, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            mapUserPreferences(preferences)
        }

    suspend fun updateShowCompletedTasks(showCompletedTasks: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHOW_COMPLETED_TASKS] = showCompletedTasks
        }
    }

    val lastPreferenceWithBlocking: Boolean get() = runBlocking {
        dataStore.data.map { preferences ->
            preferences[PreferencesKeys.SHOW_COMPLETED_TASKS] ?: true
        }.first()
    }

    suspend fun fetchInitialPreferences() =
        mapUserPreferences(dataStore.data.first().toPreferences())

    private fun mapUserPreferences(preferences: Preferences): UserPreferences {

        // Get our show completed value, defaulting to false if not set:
        val showCompletedTasks = preferences[PreferencesKeys.SHOW_COMPLETED_TASKS] ?: true
        return UserPreferences(showCompletedTasks)
    }
}
