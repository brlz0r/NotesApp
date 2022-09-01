package ru.kiruxadance.notesapp.note.domain.use_case.auth

import ru.kiruxadance.notesapp.note.domain.model.Auth
import ru.kiruxadance.notesapp.note.domain.model.Token
import ru.kiruxadance.notesapp.note.domain.repository.AuthRepository
import java.lang.Exception

class LoginUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(auth: Auth): Token? {
        return try {
            repository.login(auth)
        } catch(e: Exception) {
            println(e)
            null;
        }
    }
}