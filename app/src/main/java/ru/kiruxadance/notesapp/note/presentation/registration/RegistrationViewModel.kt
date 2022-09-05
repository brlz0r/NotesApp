package ru.kiruxadance.notesapp.note.presentation.registration

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.kiruxadance.notesapp.common.Resource
import ru.kiruxadance.notesapp.note.domain.model.Auth
import ru.kiruxadance.notesapp.note.domain.use_case.auth.AuthUseCases
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _username = mutableStateOf("")
    val username: State<String> = _username

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _repeatedPassword = mutableStateOf("")
    val repeatedPassword: State<String> = _repeatedPassword

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _state = mutableStateOf(RegistrationState())
    val state: State<RegistrationState> = _state


    fun onEvent(event: RegistrationEvent) {
        when(event) {
            is RegistrationEvent.EnteredPassword -> {
                _password.value = event.value
            }
            is RegistrationEvent.EnteredRepeatedPassword -> {
                _repeatedPassword.value = event.value
            }
            is RegistrationEvent.EnteredUsername -> {
                _username.value = event.value
            }
            RegistrationEvent.Registration -> {
                if (password.value == repeatedPassword.value) {
                    registration()
                } else {
                    viewModelScope.launch {
                        _eventFlow.emit(
                            UiEvent.ShowErrorSnackBar("Password and repeated password not equals"))
                    }
                }
            }
            RegistrationEvent.ChangePasswordVisible -> {
                _state.value = _state.value.copy(
                    isVisiblePassword = !_state.value.isVisiblePassword
                )
            }
            RegistrationEvent.ChangeRepeatedPasswordVisible -> {
                _state.value = _state.value.copy(
                    isVisibleRepeatedPassword = !_state.value.isVisibleRepeatedPassword
                )
            }
        }
    }

    fun registration() {
        viewModelScope.launch {
            authUseCases.registration(Auth(username = username.value, password = password.value))
                .collectLatest {
                when(it) {
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            registrationFormState = RegistrationFormState.Registration
                        )
                        _eventFlow.emit(UiEvent.ShowErrorSnackBar(it.message ?: "An unexpected error occured"))
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            registrationFormState = RegistrationFormState.Loading
                        )
                    }
                    is Resource.Success -> {
                        if (it.data!!) {
                            _eventFlow.emit(UiEvent.LoginScreen)
                        } else {
                            _eventFlow.emit(UiEvent.ShowErrorSnackBar("User is exist"))
                        }
                        _state.value = _state.value.copy(
                            registrationFormState = RegistrationFormState.Registration
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowErrorSnackBar(val message: String): UiEvent()
        object LoginScreen: UiEvent()
    }
}