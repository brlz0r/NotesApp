package ru.kiruxadance.notesapp.note.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.kiruxadance.notesapp.common.Resource
import ru.kiruxadance.notesapp.note.data.utils.TokenProvider
import ru.kiruxadance.notesapp.note.domain.model.Note
import ru.kiruxadance.notesapp.note.domain.model.Token
import ru.kiruxadance.notesapp.note.domain.model.User
import ru.kiruxadance.notesapp.note.domain.use_case.note.NoteUseCases
import ru.kiruxadance.notesapp.note.domain.use_case.user.UserUseCases
import ru.kiruxadance.notesapp.note.domain.util.NoteOrder
import ru.kiruxadance.notesapp.note.domain.util.OrderType
import ru.kiruxadance.notesapp.note.presentation.add_edit_note.AddEditNoteViewModel
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    private val userUseCases: UserUseCases,
    private val tokenProvider: TokenProvider
): ViewModel() {
    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    private var recentlyDeletedNote: Note? = null

    private var getNotesJob: Job? = null

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getUser()
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.Order -> {
                if (state.value.noteOrder::class == event.noteOrder::class &&
                    state.value.noteOrder.orderType == event.noteOrder.orderType
                ) {
                    return
                }
                getNotes(event.noteOrder)
            }
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    deleteNote(event.note.id ?: return@launch)
                    recentlyDeletedNote = event.note
                }
            }
            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    restoreNote(recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null
                }
            }
            is NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
            NotesEvent.Logout -> {
                viewModelScope.launch {
                    tokenProvider.saveTokens(Token(accessToken = "", refreshToken = ""))
                    tokenProvider.getTokens().collectLatest {
                        println(it.accessToken)
                    }
                }
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getNotes(noteOrder).onEach {
                when(it) {
                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            notes = it.data ?: emptyList(),
                            noteOrder = noteOrder,
                            notesListState = NotesListState.NotesList
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            notesListState = NotesListState.Loading
                        )
                    }
                    is Resource.Error -> {
                        _state.value = state.value.copy(
                            notesListState = NotesListState.NotesList
                        )
                       _eventFlow.emit(UiEvent.ShowSnackbar(it.message ?: "An unexpected error occured"))
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun restoreNote(note: Note) {
        viewModelScope.launch {
            noteUseCases.addNote(note).collectLatest{
                when(it) {
                    is Resource.Success -> {
                        getNotes(NoteOrder.Date(OrderType.Descending))
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            notesListState = NotesListState.Loading
                        )
                    }
                    is Resource.Error -> {
                        getNotes(NoteOrder.Date(OrderType.Descending))
                        _eventFlow.emit(UiEvent.ShowSnackbar(it.message ?: "An unexpected error occured"))
                    }
                }
            }
        }
    }

    private fun deleteNote(id: String) {
        viewModelScope.launch {
            noteUseCases.deleteNote(id).collectLatest{
                when(it) {
                    is Resource.Success -> {
                        getNotes(NoteOrder.Date(OrderType.Descending))
                        _eventFlow.emit(UiEvent.ShowSnackBarWithButton("Note deleted", "Undo"))
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            notesListState = NotesListState.Loading
                        )
                    }
                    is Resource.Error -> {
                        getNotes(NoteOrder.Date(OrderType.Descending))
                        _eventFlow.emit(UiEvent.ShowSnackbar(it.message ?: "An unexpected error occured"))
                    }
                }
            }
        }
    }

    private fun getUser() {
        viewModelScope.launch {
            userUseCases.getUser().collectLatest{
                when(it) {
                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            user = it.data ?: User(userName = "username")
                        )
                    }
                    is Resource.Loading -> {
                        println("loading")
                    }
                    is Resource.Error -> {
                        _eventFlow.emit(UiEvent.ShowSnackbar(it.message ?: "An unexpected error occured"))
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        data class ShowSnackBarWithButton(val message: String, val actionLabel: String): UiEvent()
    }
}