package ru.kiruxadance.notesapp.note.presentation.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.kiruxadance.notesapp.note.data.utils.TokenProvider
import ru.kiruxadance.notesapp.note.domain.model.Auth
import ru.kiruxadance.notesapp.note.domain.use_case.auth.AuthUseCases
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val tokenProvider: TokenProvider
): ViewModel() {

    private val _username = mutableStateOf("")
    val username: State<String> = _username

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: LoginEvent) {
        when(event) {
            is LoginEvent.EnteredUsername -> {
                _username.value = event.value
            }
            is LoginEvent.EnteredPassword -> {
                _password.value = event.value
            }
            LoginEvent.Login -> {
                viewModelScope.launch {
                    var token = authUseCases.login(Auth(_username.value, _password.value))
                    if (token == null) {
                        _eventFlow.emit(UiEvent.ShowErrorSnackBar(message = "User or password incorrect"))
                    } else {
                        tokenProvider.saveTokens(token)
                        _eventFlow.emit(UiEvent.NotesScreen)
                        tokenProvider.getTokens().collectLatest {
                            println(it.refreshToken)
                        }
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowErrorSnackBar(val message: String): UiEvent()
        object NotesScreen: UiEvent()
    }
}