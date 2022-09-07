package ru.kiruxadance.notesapp.note.data.repository

import ru.kiruxadance.notesapp.note.domain.model.Note
import ru.kiruxadance.notesapp.note.domain.repository.NoteRepository

class FakeNoteRepository : NoteRepository {

    private val notes = mutableListOf<Note>()

    override suspend fun getNotes(): List<Note> {
        return notes
    }

    override suspend fun getNoteById(id: String): Note? {
        return notes.find { it.id == id }
    }

    override suspend fun insertNote(note: Note): Boolean {
        return notes.add(note)
    }

    override suspend fun updateNote(id: String, note: Note): Boolean {
        val noteFind = notes.find { it.id == id }
        noteFind.let {
            it?.copy(
                title = note.title,
                content = note.content,
                timestamp = note.timestamp,
                pathWrappers = note.pathWrappers
            )
        }
        return true
    }

    override suspend fun deleteNote(id: String): Boolean {
        val note = notes.find { it.id == id }
        note.let {
            return notes.remove(it)
        }
    }
}