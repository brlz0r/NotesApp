package ru.kiruxadance.notesapp.note.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.kiruxadance.notesapp.note.presentation.add_edit_note.AddEditNoteScreen
import ru.kiruxadance.notesapp.note.presentation.login.LoginScreen
import ru.kiruxadance.notesapp.note.presentation.notes.NotesScreen
import ru.kiruxadance.notesapp.note.presentation.registration.RegistrationScreen
import ru.kiruxadance.notesapp.note.presentation.splash.SplashScreen
import ru.kiruxadance.notesapp.note.presentation.util.Screen
import ru.kiruxadance.notesapp.ui.theme.NoteAppTheme

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteAppTheme {
                Surface(
                    color = MaterialTheme.colors.primary
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.SplashScreen.route
                    ) {
                        composable(route = Screen.SplashScreen.route) {
                            SplashScreen(navController = navController)
                        }
                        composable(route = Screen.LoginScreen.route) {
                            LoginScreen(navController = navController)
                        }
                        composable(route = Screen.RegistrationScreen.route) {
                            RegistrationScreen(navController = navController)
                        }
                        composable(route = Screen.NotesScreen.route) {
                            NotesScreen(navController = navController)
                        }
                        composable(
                            route = Screen.AddEditNoteScreen.route +
                                    "?noteId={noteId}&noteColor={noteColor}",
                            arguments = listOf(
                                navArgument(
                                    name = "noteId"
                                ) {
                                    type = NavType.StringType
                                    defaultValue = ""
                                },
                            )
                        ) {
                            AddEditNoteScreen(
                                navController = navController,
                            )
                        }
                    }
                }
            }
        }
    }
}