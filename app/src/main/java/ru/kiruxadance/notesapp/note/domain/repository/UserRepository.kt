package ru.kiruxadance.notesapp.note.domain.repository

import ru.kiruxadance.notesapp.note.domain.model.User

interface UserRepository {
    suspend fun getUser(token: String) : User?
}