package ru.kiruxadance.notesapp.note.data.data_source

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import ru.kiruxadance.notesapp.note.domain.model.Auth
import ru.kiruxadance.notesapp.note.domain.model.Note

interface NoteService {
    @POST("Note")
    suspend fun addNote(@Body note: Note) : Response<ResponseBody>

    @PUT("Note")
    suspend fun updateNote(@Query("id") id: String, @Body note: Note) : Response<ResponseBody>

    @DELETE("Note")
    suspend fun deleteNote(@Query("id") id: String) : Response<ResponseBody>

    @GET("Note")
    suspend fun getNote(@Query("id") id: String) : Note

    @GET("Note/user")
    suspend fun getNotes() : List<Note>
}