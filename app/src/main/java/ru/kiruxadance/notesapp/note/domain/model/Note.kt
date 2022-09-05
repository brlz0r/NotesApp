package ru.kiruxadance.notesapp.note.domain.model

import ru.kiruxadance.notesapp.note.presentation.add_edit_note.utils.PathWrapper

data class Note(
    val title: String,
    val content: String,
    val timestamp: Long,
    val pathWrappers: List<PathWrapper>,
    val id: String?
)
class InvalidNoteException(message: String): Exception(message)