package ru.kiruxadance.notesapp.note.presentation.registration

import ru.kiruxadance.notesapp.note.presentation.login.LoginEvent

sealed class RegistrationEvent {
    data class EnteredUsername(val value: String): RegistrationEvent()
    data class EnteredPassword(val value: String): RegistrationEvent()
    data class EnteredRepeatedPassword(val value: String): RegistrationEvent()

    object ChangePasswordVisible: RegistrationEvent()
    object ChangeRepeatedPasswordVisible: RegistrationEvent()
    object Registration: RegistrationEvent()
}