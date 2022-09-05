package ru.kiruxadance.notesapp.note.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.kiruxadance.notesapp.note.domain.model.Note

interface NoteRepository {
    suspend fun getNotes(): List<Note>

    suspend fun getNoteById(id: String): Note

    suspend fun insertNote(note: Note) : Boolean

    suspend fun updateNote(id: String, note: Note) : Boolean

    suspend fun deleteNote(id: String) : Boolean
}