package ru.kiruxadance.notesapp.note.data.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import ru.kiruxadance.notesapp.note.domain.model.Token

const val DataStore_NAME = "Tokens"

val Context.datastore : DataStore<Preferences> by  preferencesDataStore(name = DataStore_NAME)

class TokenProvider(
    private val context: Context
) {
    companion object{
        val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
        val REFRESH_TOKEN = stringPreferencesKey("REFRESH_TOKEN")
    }

    suspend fun saveTokens(token: Token) {
        context.datastore.edit { tokens ->
            tokens[ACCESS_TOKEN] = token.accessToken
            tokens[REFRESH_TOKEN]= token.refreshToken
        }
    }

    suspend fun getTokens() = context.datastore.data.map { tokens ->
        Token(
            accessToken = tokens[ACCESS_TOKEN]!!,
            refreshToken = tokens[REFRESH_TOKEN]!!
        )
    }
}