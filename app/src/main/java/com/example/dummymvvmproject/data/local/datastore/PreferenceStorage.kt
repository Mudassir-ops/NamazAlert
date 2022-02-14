package com.example.dummymvvmproject.data.local.datastore

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

interface PreferenceStorage {

    suspend fun clearPreferenceStorage()
    suspend fun writeLatLng(latLngModel: String)
    val getLatLng : Flow<String>

}

@Singleton
class AppPrefsStorage @Inject constructor(
    @ApplicationContext context: Context,
) : PreferenceStorage {
    // since @Singleton scope is used, dataStore will have the same instance every time
    private val dataStore: DataStore<Preferences> = context.createDataStore(name = "AppPrefStorage")


    override suspend fun clearPreferenceStorage() {
        dataStore.edit {
            it.clear()
        }
    }

    override suspend fun writeLatLng(latLngModel: String) {
        dataStore.setValue(PreferencesKeys.LATLNG, latLngModel)
    }

    override val getLatLng: Flow<String>
        get() = dataStore.getValueAsFlow(PreferencesKeys.LATLNG,"")

    private suspend fun <T> DataStore<Preferences>.setValue(
        key: Preferences.Key<T>,
        value: T
    ) {
        this.edit { preferences ->
            // save the value in prefs
            preferences[key] = value
        }
    }
    private fun <T> DataStore<Preferences>.getValueAsFlow(
        key: Preferences.Key<T>,
        defaultValue: T
    ): Flow<T> {
        return this.data.catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                // we try again to store the value in the map operator
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            // return the default value if it doesn't exist in the storage
            preferences[key] ?: defaultValue
        }
    }

    private object PreferencesKeys {
        val LATLNG = preferencesKey<String>("pref_latlng_key")
    }
}