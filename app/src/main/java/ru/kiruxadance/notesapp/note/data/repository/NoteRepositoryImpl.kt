package ru.kiruxadance.notesapp.note.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.kiruxadance.notesapp.note.data.data_source.NoteService
import ru.kiruxadance.notesapp.note.domain.model.Note
import ru.kiruxadance.notesapp.note.domain.repository.NoteRepository

class NoteRepositoryImpl(
    private val noteService: NoteService
) : NoteRepository {

    override suspend fun getNotes(): List<Note> {
        return noteService.getNotes()
    }

    override suspend fun getNoteById(id: String): Note {
       return noteService.getNote(id)
    }

    override suspend fun insertNote(note: Note) : Boolean {
        return noteService.addNote(note).isSuccessful
    }

    override suspend fun updateNote(id: String, note: Note) : Boolean {
        return noteService.updateNote(id, note).isSuccessful
    }

    override suspend fun deleteNote(id: String) : Boolean{
        return noteService.deleteNote(id).isSuccessful
    }
}