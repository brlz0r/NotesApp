package ru.kiruxadance.notesapp.note.presentation.add_edit_note

import androidx.compose.ui.graphics.Color

data class DrawLineState(
    val currentColor: Color = Color.Red,
    val currentSize: Int = 10,
)