package ru.kiruxadance.notesapp.note.presentation.registration


data class RegistrationState(
    val registrationFormState: RegistrationFormState = RegistrationFormState.Registration,
    val isVisiblePassword: Boolean = false,
    val isVisibleRepeatedPassword: Boolean = false
)