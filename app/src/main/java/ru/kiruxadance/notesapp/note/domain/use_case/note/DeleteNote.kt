package ru.kiruxadance.notesapp.note.domain.use_case.note

import ru.kiruxadance.notesapp.note.domain.model.Note
import ru.kiruxadance.notesapp.note.domain.repository.NoteRepository

class DeleteNote(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}