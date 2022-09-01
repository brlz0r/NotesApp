package ru.kiruxadance.notesapp.note.domain.repository

import ru.kiruxadance.notesapp.note.domain.model.Auth
import ru.kiruxadance.notesapp.note.domain.model.RefreshToken
import ru.kiruxadance.notesapp.note.domain.model.Token

interface AuthRepository {
    suspend fun login(auth: Auth) : Token?
    suspend fun registration(auth: Auth) : Boolean
    suspend fun refresh(refreshToken: RefreshToken) : Token?
}