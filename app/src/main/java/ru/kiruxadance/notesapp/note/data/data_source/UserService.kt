package ru.kiruxadance.notesapp.note.data.data_source

import retrofit2.http.GET
import ru.kiruxadance.notesapp.note.domain.model.User

interface UserService {
    @GET("User")
    suspend fun getUser() : User
}