package ru.kiruxadance.notesapp.note.presentation.notes

import ru.kiruxadance.notesapp.note.domain.model.Note
import ru.kiruxadance.notesapp.note.domain.model.User
import ru.kiruxadance.notesapp.note.domain.util.NoteOrder
import ru.kiruxadance.notesapp.note.domain.util.OrderType

data class NotesState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false,
    val notesListState: NotesListState = NotesListState.Loading,
    val user: User = User(userName = "Username")
)
