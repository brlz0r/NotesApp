package ru.kiruxadance.notesapp.note.presentation.add_edit_note

import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.geometry.Offset
import ru.kiruxadance.notesapp.note.domain.model.Point

sealed class AddEditNoteEvent {
    data class EnteredTitle(val value: String): AddEditNoteEvent()
    data class ChangeTitleFocus(val focusState: FocusState): AddEditNoteEvent()
    data class EnteredContent(val value: String): AddEditNoteEvent()
    data class ChangeContentFocus(val focusState: FocusState): AddEditNoteEvent()
    data class Draw(val offset: Offset): AddEditNoteEvent()

    object ChangeEditTypeState: AddEditNoteEvent()
    object SaveNote: AddEditNoteEvent()
}