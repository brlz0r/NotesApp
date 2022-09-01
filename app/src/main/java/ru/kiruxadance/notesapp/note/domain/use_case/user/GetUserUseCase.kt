package ru.kiruxadance.notesapp.note.domain.use_case.user

import ru.kiruxadance.notesapp.note.domain.model.User
import ru.kiruxadance.notesapp.note.domain.repository.UserRepository
import java.lang.Exception

class GetUserUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(token: String): User? {
        return try {
            repository.getUser(token)
        } catch(e: Exception) {
            println(e)
            null;
        }
    }
}