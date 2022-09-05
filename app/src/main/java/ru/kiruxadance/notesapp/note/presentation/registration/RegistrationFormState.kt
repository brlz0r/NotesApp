package ru.kiruxadance.notesapp.note.presentation.registration

sealed class RegistrationFormState {
    object Registration: RegistrationFormState()
    object Loading: RegistrationFormState()
}
