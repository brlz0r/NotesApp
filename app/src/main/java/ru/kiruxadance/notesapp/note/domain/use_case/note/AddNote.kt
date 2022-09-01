package ru.kiruxadance.notesapp.note.domain.use_case.note

import ru.kiruxadance.notesapp.note.domain.model.InvalidNoteException
import ru.kiruxadance.notesapp.note.domain.model.Note
import ru.kiruxadance.notesapp.note.domain.repository.NoteRepository
import kotlin.jvm.Throws

class AddNote(
    private val repository: NoteRepository
) {
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if (note.title.isBlank()) {
            throw InvalidNoteException("The title of the note cant`t be empty")
        }

        if (note.content.isBlank()) {
            throw InvalidNoteException("The title of the note cant`t be empty")
        }
        repository.insertNote(note)
    }
}