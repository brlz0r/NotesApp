package ru.kiruxadance.notesapp.note.data.repository

import ru.kiruxadance.notesapp.note.data.data_source.AuthService
import ru.kiruxadance.notesapp.note.domain.model.Auth
import ru.kiruxadance.notesapp.note.domain.model.RefreshToken
import ru.kiruxadance.notesapp.note.domain.model.Token
import ru.kiruxadance.notesapp.note.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authService: AuthService
) : AuthRepository {
    override suspend fun login(auth: Auth): Token? {
        return authService.login(auth).body()
    }

    override suspend fun registration(auth: Auth): Boolean {
        return authService.registration(auth).code() == 200
    }

    override suspend fun refresh(refreshToken: RefreshToken): Token? {
        return authService.refresh(refreshToken).body()
    }
}