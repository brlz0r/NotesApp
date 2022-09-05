package ru.kiruxadance.notesapp.note.domain.use_case.user

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import ru.kiruxadance.notesapp.common.Resource
import ru.kiruxadance.notesapp.note.domain.model.User
import ru.kiruxadance.notesapp.note.domain.repository.UserRepository
import java.io.IOException
import java.lang.Exception

class GetUser(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): Flow<Resource<User>> = flow {
        try {
            emit(Resource.Loading())
            val user = repository.getUser()
            emit(Resource.Success(user))
        } catch(e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch(e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}