package ru.kiruxadance.notesapp.note.data.repository

import kotlinx.coroutines.flow.Flow
import ru.kiruxadance.notesapp.note.data.data_source.NoteDao
import ru.kiruxadance.notesapp.note.domain.model.Note
import ru.kiruxadance.notesapp.note.domain.repository.NoteRepository

class NoteRepositoryImpl(
    private val dao: NoteDao
): NoteRepository
{
    override fun getNotes(): Flow<List<Note>> {
        return dao.getNotes()
    }

    override suspend fun getNoteById(id: Int): Note? {
       return dao.getNoteById(id)
    }

    override suspend fun insertNote(note: Note) {
        return dao.insertNote(note)
    }

    override suspend fun deleteNote(note: Note) {
       return dao.deleteNote(note)
    }
}