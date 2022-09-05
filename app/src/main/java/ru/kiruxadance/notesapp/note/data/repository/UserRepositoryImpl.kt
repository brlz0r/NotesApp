package ru.kiruxadance.notesapp.note.data.repository

import ru.kiruxadance.notesapp.note.data.data_source.UserService
import ru.kiruxadance.notesapp.note.domain.model.User
import ru.kiruxadance.notesapp.note.domain.repository.UserRepository

class UserRepositoryImpl(
    private val userService: UserService,
) : UserRepository{

    override suspend fun getUser(): User {
        return userService.getUser()
    }
}