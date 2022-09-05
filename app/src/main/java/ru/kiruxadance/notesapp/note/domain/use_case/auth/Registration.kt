package ru.kiruxadance.notesapp.note.domain.use_case.auth

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import ru.kiruxadance.notesapp.common.Resource
import ru.kiruxadance.notesapp.note.domain.model.Auth
import ru.kiruxadance.notesapp.note.domain.repository.AuthRepository
import java.io.IOException

class Registration(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(auth: Auth) : Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            val result = repository.registration(auth)
            emit(Resource.Success(result))
        } catch(e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch(e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}