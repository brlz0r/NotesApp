package ru.kiruxadance.notesapp.note.domain.use_case.note

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import ru.kiruxadance.notesapp.common.Resource
import ru.kiruxadance.notesapp.note.domain.model.InvalidNoteException
import ru.kiruxadance.notesapp.note.domain.model.Note
import ru.kiruxadance.notesapp.note.domain.repository.NoteRepository
import java.io.IOException
import kotlin.jvm.Throws

class UpdateNote(
    private val repository: NoteRepository
) {
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(id: String, note: Note) : Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            repository.updateNote(id, note)
            emit(Resource.Success(true))
        } catch(e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch(e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}