package ru.kiruxadance.notesapp.note.presentation.add_edit_note

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.kiruxadance.notesapp.note.domain.model.InvalidNoteException
import ru.kiruxadance.notesapp.note.domain.model.Note
import ru.kiruxadance.notesapp.note.domain.use_case.note.NoteUseCases
import ru.kiruxadance.notesapp.note.presentation.add_edit_note.controllers.DrawController
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _noteTitle = mutableStateOf(NoteTextFieldState(
        hint = "Enter title..."
    ))
    val noteTitle: State<NoteTextFieldState> = _noteTitle

    private val _noteContent = mutableStateOf(NoteTextFieldState(
        hint = "Enter some content"
    ))
    val noteContent: State<NoteTextFieldState> = _noteContent

    private val _noteEditType = mutableStateOf(NoteEditTypeState(
        isEditTypeDraw = false
    ))
    val noteEditType: State<NoteEditTypeState> = _noteEditType

    private val _drawController = mutableStateOf(DrawController())
    val drawController: State<DrawController> = _drawController

    private val _drawBar = mutableStateOf(DrawBarState(
        sizeBarVisibility = false,
        colorBarVisibility = false,
        colorIsBg = false
    ))
    val drawBar: State<DrawBarState> = _drawBar

    private val _drawLine = mutableStateOf(DrawLineState(
        currentColor = Color.Red,
        currentSize = 10,
    ))
    val drawLine: State<DrawLineState> = _drawLine

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if(noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.also { note ->
                        currentNoteId = note.id
                        _drawController.value.initPath(note.pathWrappers)
                        _noteTitle.value = noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )
                        _noteContent.value = _noteContent.value.copy(
                            text = note.content,
                            isHintVisible = false
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        when(event) {
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            noteTitle.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = _noteContent.value.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContent.value = _noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            _noteContent.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNote(
                            Note(
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                timestamp = System.currentTimeMillis(),
                                id = currentNoteId,
                                pathWrappers = _drawController.value.pathList
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch(e: InvalidNoteException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )
                    }
                }
            }
            AddEditNoteEvent.ChangeEditTypeState -> {
                _noteEditType.value = _noteEditType.value.copy(
                    isEditTypeDraw = !_noteEditType.value.isEditTypeDraw,
                    appBarImage = if (!_noteEditType.value.isEditTypeDraw)
                        Icons.Filled.Edit else Icons.Filled.Draw,
                )
            }
            is AddEditNoteEvent.ChangeColorBarVisibility -> {
                //println(event.visibility)
                _drawBar.value = _drawBar.value.copy(
                    colorBarVisibility = event.visibility
                )
            }
            is AddEditNoteEvent.ChangeColorIsBg -> {
                _drawBar.value = _drawBar.value.copy(
                    colorIsBg = event.colorIsBg
                )
            }
            is AddEditNoteEvent.ChangeCurrentColor -> {
                _drawLine.value = _drawLine.value.copy(
                    currentColor = event.color
                )
            }
            is AddEditNoteEvent.ChangeCurrentSize -> {
                _drawLine.value = _drawLine.value.copy(
                    currentSize = event.size
                )
            }
            is AddEditNoteEvent.ChangeSizeBarVisibility -> {
                _drawBar.value = _drawBar.value.copy(
                    sizeBarVisibility = event.visibility
                )
            }
            is AddEditNoteEvent.SetDrawController -> {
                _drawController.value = event.drawController
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        object SaveNote: UiEvent()
    }
}