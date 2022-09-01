package ru.kiruxadance.notesapp.note.presentation.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import ru.kiruxadance.notesapp.note.presentation.util.Screen

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val usernameState = viewModel.username.value
    val passwordState = viewModel.password.value

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is LoginViewModel.UiEvent.ShowErrorSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is LoginViewModel.UiEvent.NotesScreen -> {
                    navController.navigate(
                        Screen.NotesScreen.route
                    ){
                        popUpTo(Screen.LoginScreen.route) {
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
                Text("Login Screen", fontSize = 32.sp)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = usernameState,
                    placeholder = { Text("Username") },
                    leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Username") },
                    onValueChange = {viewModel.onEvent(LoginEvent.EnteredUsername(it))})
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = passwordState,
                    placeholder = { Text("Password") },
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password") },
                    onValueChange = {viewModel.onEvent(LoginEvent.EnteredPassword(it))})
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    viewModel.onEvent(LoginEvent.Login)
                }) {
                    Text(text = "Login")
                }
                TextButton(onClick = {
                    navController.navigate(
                        Screen.RegistrationScreen.route
                    )
                }) {
                    Text("Don't have an account?")
                }
            }
        }
    }
}