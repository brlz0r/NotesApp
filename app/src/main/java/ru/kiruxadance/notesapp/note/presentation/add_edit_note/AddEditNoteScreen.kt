package ru.kiruxadance.notesapp.note.presentation.add_edit_note

import android.view.MotionEvent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.kiruxadance.notesapp.note.domain.model.Point
import ru.kiruxadance.notesapp.note.presentation.add_edit_note.components.DrawBox
import ru.kiruxadance.notesapp.note.presentation.add_edit_note.components.TransparentHintTextField
import ru.kiruxadance.notesapp.note.presentation.add_edit_note.controllers.rememberDrawController
import ru.kiruxadance.notesapp.note.presentation.add_edit_note.utils.createPath
import ru.kiruxadance.notesapp.note.presentation.notes.NotesEvent
import ru.kiruxadance.notesapp.note.presentation.notes.components.OrderSection

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {
    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value
    val editTypeState = viewModel.noteEditType

    val drawController = rememberDrawController()

    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is AddEditNoteViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is AddEditNoteViewModel.UiEvent.SaveNote -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Add or update Note") },
                actions = {
                    IconButton(onClick = {
                        viewModel.onEvent(AddEditNoteEvent.ChangeEditTypeState)
                    }) {
                        Icon(editTypeState.value.appBarImage, contentDescription = "Localized description")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(AddEditNoteEvent.SaveNote)
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save note")
            }
        },
        scaffoldState = scaffoldState
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {drawController.insertNewPath(Offset(it.x, it.y))}
                            MotionEvent.ACTION_MOVE -> {drawController.updateLatestPath(Offset(it.x, it.y))}
                            MotionEvent.ACTION_UP -> {}
                            else ->  false
                        }
                        editTypeState.value.isEditTypeDraw
                    }

            ){
                Canvas(modifier = Modifier.zIndex(100f)) {
                    drawController.pathList.forEach { pw ->
                        drawPath(
                            createPath(pw.points),
                            color = pw.strokeColor,
                            alpha = pw.alpha,
                            style = Stroke(
                                width = pw.strokeWidth,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                TransparentHintTextField(
                    text = titleState.text,
                    hint = titleState.hint,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    onValueChange = {
                        viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it))
                    },
                    onFocusChange = {
                        viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(it))
                    },
                    isHintVisible = titleState.isHintVisible,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.h5,
                    focusable = !editTypeState.value.isEditTypeDraw
                )
                Spacer(modifier = Modifier.height(16.dp))
                TransparentHintTextField(
                    text = contentState.text,
                    hint = contentState.hint,
                    onValueChange = {
                        viewModel.onEvent(AddEditNoteEvent.EnteredContent(it))
                    },
                    onFocusChange = {
                        viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(it))
                    },
                    isHintVisible = contentState.isHintVisible,
                    textStyle = MaterialTheme.typography.body1,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .weight(1f, true),
                    focusable = !editTypeState.value.isEditTypeDraw
                )
            }
        }
    }
}