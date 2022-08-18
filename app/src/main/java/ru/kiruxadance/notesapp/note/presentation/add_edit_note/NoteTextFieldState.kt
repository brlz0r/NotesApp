package ru.kiruxadance.notesapp.note.presentation.add_edit_note

import androidx.compose.ui.graphics.Color

data class NoteTextFieldState(
    val text: String = "",
    val hint: String = "",
    val textSize: Int = 16,
    val textColor: Color = Color.Black,
    val isHintVisible: Boolean = true,
)