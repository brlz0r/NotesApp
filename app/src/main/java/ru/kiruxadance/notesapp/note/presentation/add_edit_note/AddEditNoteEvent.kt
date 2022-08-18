package ru.kiruxadance.notesapp.note.presentation.add_edit_note

import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.Color
import ru.kiruxadance.notesapp.note.presentation.add_edit_note.controllers.DrawController

sealed class AddEditNoteEvent {
    data class EnteredTitle(val value: String): AddEditNoteEvent()
    data class ChangeTitleFocus(val focusState: FocusState): AddEditNoteEvent()
    data class EnteredContent(val value: String): AddEditNoteEvent()
    data class ChangeContentFocus(val focusState: FocusState): AddEditNoteEvent()
    data class ChangeColorBarVisibility(val visibility: Boolean): AddEditNoteEvent()
    data class ChangeSizeBarVisibility(val visibility: Boolean): AddEditNoteEvent()
    data class ChangeCurrentColor(val color: Color): AddEditNoteEvent()
    data class ChangeCurrentSize(val size: Int): AddEditNoteEvent()
    data class ChangeColorIsBg(val colorIsBg: Boolean): AddEditNoteEvent()
    data class SetDrawController(val drawController: DrawController): AddEditNoteEvent()

    object ChangeEditTypeState: AddEditNoteEvent()
    object SaveNote: AddEditNoteEvent()
}