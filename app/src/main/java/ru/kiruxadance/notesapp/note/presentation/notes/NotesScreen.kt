package ru.kiruxadance.notesapp.note.presentation.notes

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.kiruxadance.notesapp.note.presentation.add_edit_note.AddEditNoteEvent
import ru.kiruxadance.notesapp.note.presentation.add_edit_note.AddEditNoteViewModel
import ru.kiruxadance.notesapp.note.presentation.notes.components.*
import ru.kiruxadance.notesapp.note.presentation.util.Screen

@Composable
fun NotesScreen(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is NotesViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is NotesViewModel.UiEvent.ShowSnackBarWithButton -> {
                    val result = scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.actionLabel
                    )
                    if(result == SnackbarResult.ActionPerformed) {
                        viewModel.onEvent(NotesEvent.RestoreNote)
                    }
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddEditNoteScreen.route)
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add note")
            }
        },
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerContent = {
            DrawerHeader(state.user.userName)
            DrawerBody(
                items = listOf(
                    MenuItem(
                        id = "home",
                        title = "Home",
                        contentDescription = "Go to home screen",
                        icon = Icons.Default.Home,
                        color = Color.Black
                    ),
                    MenuItem(
                        id = "logout",
                        title = "Logout",
                        contentDescription = "Logout",
                        icon = Icons.Default.Logout,
                        color = Color.Red
                    ),
                ),
                onItemClick = {
                    if (it.id == "logout") {
                        viewModel.onEvent(NotesEvent.Logout)
                        navController.navigate(
                            Screen.LoginScreen.route
                        ){
                            popUpTo(Screen.NotesScreen.route) {
                                inclusive = true
                            }
                        }
                    }
                }
            )
        },
        scaffoldState = scaffoldState
    ) {
            contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            scope.launch {
                                scaffoldState.drawerState.open()
                            }
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Sort"
                        )
                    }
                    Text(
                        text = "Your note",
                        style = MaterialTheme.typography.h4
                    )
                    IconButton(
                        onClick = {
                            viewModel.onEvent(NotesEvent.ToggleOrderSection)
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sort,
                            contentDescription = "Sort"
                        )
                    }
                }
                AnimatedVisibility(
                    visible = state.isOrderSectionVisible,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    OrderSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        noteOrder = state.noteOrder,
                        onOrderChange = {
                            viewModel.onEvent(NotesEvent.Order(it))
                        }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Crossfade(targetState = state) { screen ->
                    when (screen.notesListState) {
                        NotesListState.NotesList -> {
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                items(state.notes) { note ->
                                    NoteItem(
                                        note = note,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                navController.navigate(
                                                    Screen.AddEditNoteScreen.route +
                                                            "?noteId=${note.id}"
                                                )
                                            }
                                            .background(Color.White),
                                        onDeleteClick = {
                                            viewModel.onEvent(NotesEvent.DeleteNote(note))
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                        }
                        NotesListState.Loading -> {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}