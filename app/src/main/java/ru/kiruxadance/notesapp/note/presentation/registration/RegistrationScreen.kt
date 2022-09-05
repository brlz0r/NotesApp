package ru.kiruxadance.notesapp.note.presentation.registration

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.R
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import ru.kiruxadance.notesapp.note.presentation.login.LoginViewModel
import ru.kiruxadance.notesapp.note.presentation.util.Screen

@Composable
fun RegistrationScreen(
    navController: NavController,
    viewModel: RegistrationViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val state = viewModel.state.value

    val usernameState = viewModel.username.value
    val passwordState = viewModel.password.value
    val repeatedPasswordState = viewModel.repeatedPassword.value

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is RegistrationViewModel.UiEvent.LoginScreen -> {
                    navController.navigate(
                        Screen.LoginScreen.route
                    ){
                        popUpTo(Screen.RegistrationScreen.route) {
                            inclusive = true
                        }
                    }
                }
                is RegistrationViewModel.UiEvent.ShowErrorSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
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
                Crossfade(targetState = state) {
                    when(it.registrationFormState) {
                        RegistrationFormState.Loading -> {
                            CircularProgressIndicator()
                        }
                        RegistrationFormState.Registration -> {
                            Column(
                                Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Registration Screen", fontSize = 32.sp)
                                Spacer(modifier = Modifier.height(16.dp))
                                OutlinedTextField(value = usernameState,
                                    placeholder = { Text("Username") },
                                    leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Username") },
                                    onValueChange = { value ->
                                        viewModel.onEvent(RegistrationEvent.EnteredUsername(value))
                                    })
                                Spacer(modifier = Modifier.height(16.dp))
                                OutlinedTextField(
                                    value = passwordState,
                                    placeholder = { Text("Password") },
                                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password") },
                                    onValueChange = { value ->
                                        viewModel.onEvent(RegistrationEvent.EnteredPassword(value))
                                    },
                                    visualTransformation = if (state.isVisiblePassword)
                                        VisualTransformation.None else PasswordVisualTransformation(),
                                    trailingIcon =  {
                                        IconButton(
                                            onClick = { viewModel.onEvent(RegistrationEvent.ChangePasswordVisible) }) {
                                            val image = if (state.isVisiblePassword)
                                                Icons.Filled.Visibility
                                            else Icons.Filled.VisibilityOff

                                            Icon(imageVector = image, contentDescription = "Password visibility")
                                        }
                                    }
                                )

                                Spacer(modifier = Modifier.height(16.dp))
                                OutlinedTextField(value = repeatedPasswordState,
                                    placeholder = { Text("Repeated Password") },
                                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Repeat Password") },
                                    onValueChange = { value ->
                                        viewModel.onEvent(RegistrationEvent.EnteredRepeatedPassword(value))
                                    },
                                    visualTransformation = if (state.isVisibleRepeatedPassword)
                                        VisualTransformation.None else PasswordVisualTransformation(),
                                    trailingIcon =  {
                                        IconButton(
                                            onClick = { viewModel.onEvent(RegistrationEvent.ChangeRepeatedPasswordVisible) }) {
                                            val image = if (state.isVisibleRepeatedPassword)
                                                Icons.Filled.Visibility
                                            else Icons.Filled.VisibilityOff

                                            Icon(imageVector = image, contentDescription = "Password visibility")
                                        }
                                    })
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(onClick = {
                                    viewModel.onEvent(RegistrationEvent.Registration)
                                }) {
                                    Text(text = "Login")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}