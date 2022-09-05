package ru.kiruxadance.notesapp.note.presentation.notes

sealed class NoteDeleteState {
    object Delete: NoteDeleteState()
    object Loading: NoteDeleteState()
}