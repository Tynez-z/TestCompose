package com.example.testCompose.common

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.testCompose.domain.entity.language.LanguageItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LanguageDataStore (private val json: Json, private val preferences: DataStore<Preferences>) {

     val language: Flow<LanguageItem> = preferences.data
        .map { preferences ->
            val languageString = preferences[KEY_LANGUAGE]
            if (languageString != null) {
                json.decodeFromString(languageString)
            } else {
                LanguageItem.default
            }
        }

    val languageCode: Flow<String> = language.map { it.iso_639_1 }

    suspend fun onLanguageSelected(language: LanguageItem) {
        preferences.edit { preferences ->
            preferences[KEY_LANGUAGE] = json.encodeToString(language)
        }
    }

    companion object {
        val KEY_LANGUAGE = stringPreferencesKey("language")
    }
}