package ru.kiruxadance.notesapp.note.presentation.registration

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun RegistrationScreen(
    navController: NavController
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

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
                Text("Registration Screen", fontSize = 32.sp)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = "Username",
                    leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Username") },
                    onValueChange = {})
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = "Password",
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password") },
                    onValueChange = {})
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = "Repeat Password",
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Repeat Password") },
                    onValueChange = {})
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {

                }) {
                    Text(text = "Login")
                }
            }
        }
    }
}