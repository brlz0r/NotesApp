package ru.kiruxadance.notesapp.note.domain.use_case.note

data class NoteUseCases(
    val getNotes : GetNotes,
    val deleteNote: DeleteNote,
    val addNote: AddNote,
    val getNote: GetNote,
    val updateNote: UpdateNote,
)