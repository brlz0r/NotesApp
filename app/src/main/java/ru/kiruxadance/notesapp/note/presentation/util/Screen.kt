package ru.kiruxadance.notesapp.note.presentation.util

sealed class Screen(val route: String) {
    object SplashScreen: Screen("splash_screen")
    object LoginScreen: Screen("login_screen")
    object RegistrationScreen: Screen("registration_screen")
    object NotesScreen: Screen("notes_screen")
    object AddEditNoteScreen: Screen("add_edit_note_screen")
}
