package ru.kiruxadance.notesapp.note.data.data_source

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.kiruxadance.notesapp.note.domain.model.Auth
import ru.kiruxadance.notesapp.note.domain.model.RefreshToken
import ru.kiruxadance.notesapp.note.domain.model.Token

interface AuthService {
    @POST("Auth/authenticate")
    suspend fun login(@Body auth: Auth) : Response<Token>

    @POST("Auth/registration")
    suspend fun registration(@Body auth: Auth) : Response<ResponseBody>

    @POST("Auth/refresh")
    suspend fun refresh(@Body refreshToken: RefreshToken) : Response<Token>
}