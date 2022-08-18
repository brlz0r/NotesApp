package ru.kiruxadance.notesapp.note.presentation.add_edit_note


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Draw
import androidx.compose.ui.graphics.vector.ImageVector

data class NoteEditTypeState(
    val appBarImage: ImageVector = Icons.Filled.Draw,
    val isEditTypeDraw: Boolean = false,
)