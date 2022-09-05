package ru.kiruxadance.notesapp.note.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.kiruxadance.notesapp.common.Resource
import ru.kiruxadance.notesapp.note.data.utils.TokenProvider
import ru.kiruxadance.notesapp.note.domain.use_case.user.UserUseCases
import ru.kiruxadance.notesapp.note.presentation.add_edit_note.AddEditNoteViewModel
import ru.kiruxadance.notesapp.note.presentation.registration.RegistrationEvent
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userUseCases: UserUseCases
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            userUseCases.getUser().collectLatest{
                when(it) {
                    is Resource.Success -> {
                        _eventFlow.emit(UiEvent.NotesScreen)
                    }
                    is Resource.Loading -> {
                        println("Loading")
                    }
                    is Resource.Error -> {
                        _eventFlow.emit(UiEvent.LoginScreen)
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        object LoginScreen: UiEvent()
        object NotesScreen: UiEvent()
    }
}