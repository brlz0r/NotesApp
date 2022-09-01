package ru.kiruxadance.notesapp.note.presentation.splash

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Note
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import ru.kiruxadance.notesapp.note.presentation.login.LoginViewModel
import ru.kiruxadance.notesapp.note.presentation.util.Screen

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is SplashViewModel.UiEvent.LoginScreen -> {
                    navController.navigate(
                        Screen.LoginScreen.route
                    ){
                        popUpTo(Screen.SplashScreen.route) {
                            inclusive = true
                        }
                    }
                }
                is SplashViewModel.UiEvent.NotesScreen -> {
                    navController.navigate(
                        Screen.NotesScreen.route
                    ){
                        popUpTo(Screen.SplashScreen.route) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState
    ) {
            contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Filled.Note, contentDescription = "Username")
            }
        }
    }
}