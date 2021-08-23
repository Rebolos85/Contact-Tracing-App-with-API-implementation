package com.example.trazeapp.viewmodel

import androidx.annotation.UiThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trazeapp.data.model.InputError

import com.example.trazeapp.repository.UserRepository
import com.example.trazeapp.util.states.CredentialStates
import com.example.trazeapp.util.error.InputErrorTarget

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel // diri ang logic controller
class LoginFragmentViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _errorFlow =
        MutableStateFlow<CredentialStates>(CredentialStates.Idle)
    val errorFlow: Flow<CredentialStates>
        get() = _errorFlow


    @UiThread
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _errorFlow.emit(CredentialStates.Idle)
            val validateLogin = validateLogin(email, password)


            if (validateLogin.isNullOrEmpty()) {
                _errorFlow.emit(CredentialStates.Loading)
                try {
                    userRepository.login(email, password)
                    _errorFlow.emit(CredentialStates.SuccessCredentials)
                } catch (error: Exception) {
                    _errorFlow.emit(
                        CredentialStates.ValidServerErrorResponse(
                            error.localizedMessage ?: "Unknown error"
                        )
                    )

                    if (error.localizedMessage == null) {
                        _errorFlow.emit(CredentialStates.Idle)
                    }
                }
            } else {
                _errorFlow.emit(CredentialStates.ValidateError(validateLogin))

            }
        }
    }

    private fun validateLogin(email: String, password: String): MutableList<InputError>? {
        val errorList = mutableListOf<InputError>()

        if (email.isBlank()) {
            errorList.add(InputError(InputErrorTarget.EMAIL, "Email cannot be empty"))
        }

        if (password.isEmpty()) {
            errorList.add(InputError(InputErrorTarget.PASSWORD, "Password cannot be empty"))
        }

        return if (errorList.isEmpty()) null else errorList
    }


}

