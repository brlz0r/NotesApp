package ru.kiruxadance.notesapp.note.presentation.login

import androidx.compose.ui.focus.FocusState

sealed class LoginEvent {
    data class EnteredUsername(val value: String): LoginEvent()
    data class EnteredPassword(val value: String): LoginEvent()

    object Login: LoginEvent()
}
