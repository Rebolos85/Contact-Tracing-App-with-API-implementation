package com.example.trazeapp.util.states

sealed class OtpVerificationStates {
    object LOADING : OtpVerificationStates()
    object IDLE : OtpVerificationStates()
    object SuccessVerification : OtpVerificationStates()
    data class Error(val message: String) : OtpVerificationStates()

    data class ValidateErrorServerResponse(val serverMessageResponse: String) :
        OtpVerificationStates()
}
