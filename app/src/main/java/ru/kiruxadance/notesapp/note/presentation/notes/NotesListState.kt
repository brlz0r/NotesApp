package ru.kiruxadance.notesapp.note.presentation.notes

sealed class NotesListState {
    object NotesList: NotesListState()
    object Loading: NotesListState()
}