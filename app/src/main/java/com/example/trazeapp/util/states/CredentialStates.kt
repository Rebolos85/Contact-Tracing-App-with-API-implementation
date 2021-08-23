package com.example.trazeapp.util.states

import android.net.Uri
import com.example.trazeapp.data.model.InputError
import com.example.trazeapp.data.model.PhotoError
import com.example.trazeapp.util.error.PhotoStates

sealed class CredentialStates {
    object Idle : CredentialStates()
    object Loading : CredentialStates()
    object SuccessAuth : CredentialStates()
    object SuccessCredentials : CredentialStates()
    object SuccessOtpSent : CredentialStates()
    data class SucessUploadPhoto(val testMessge: String) : CredentialStates()
    data class ValidServerErrorResponse(val message: String) : CredentialStates()
    data class ValidateError(var inputErrors: MutableList<InputError>?) : CredentialStates()

}