package ru.kiruxadance.notesapp.note.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.kiruxadance.notesapp.note.data.utils.TokenProvider
import ru.kiruxadance.notesapp.note.domain.use_case.user.UserUseCases
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userUseCases: UserUseCases,
    private val tokenProvider: TokenProvider
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            tokenProvider.getTokens().collectLatest {
                var user = userUseCases.getUserUseCase(it.accessToken)

                if (user == null) {
                    _eventFlow.emit(UiEvent.LoginScreen)
                } else {
                    _eventFlow.emit(UiEvent.NotesScreen)
                }
            }
        }
    }

    sealed class UiEvent {
        object LoginScreen: UiEvent()
        object NotesScreen: UiEvent()
    }
}