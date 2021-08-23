package com.example.trazeapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trazeapp.dao.ImageDao
import com.example.trazeapp.data.model.InputError
import com.example.trazeapp.data.model.Name
import com.example.trazeapp.data.model.RegisterAsIndividual
import com.example.trazeapp.other.UriUploadImageCatcher.uriPathUploadImage
import com.example.trazeapp.repository.UserRepository
import com.example.trazeapp.util.error.InputErrorTarget
import com.example.trazeapp.util.states.CredentialStates
import com.example.trazeapp.util.states.OtpVerificationStates
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtpVerificationViewModel @Inject constructor(
    private val userRepository: UserRepository,

) :
    ViewModel() {
    private val _stateFlow = MutableStateFlow<OtpVerificationStates>(OtpVerificationStates.IDLE)
    val stateFlow: Flow<OtpVerificationStates> get() = _stateFlow


    fun registerUserAccount(
        userOtpEnter: String,
        verificationOtp: String,
        registerAsIndividual: RegisterAsIndividual,
    ) {

        val validateOtp = validateUserOtp(userOtpEnter)
//        if (validateOtp.isNullOrEmpty()) {
        viewModelScope.launch {
            _stateFlow.emit(OtpVerificationStates.IDLE)
            with(userRepository) {
                with(registerAsIndividual) {
                    if (validateOtp) {
                        _stateFlow.emit(OtpVerificationStates.LOADING)
                        try {
                            verifyOtpSentToPhone(
                                userEnterOtp = userOtpEnter, verificationOtp = verificationOtp)

                            _stateFlow.emit(OtpVerificationStates.SuccessVerification)
                        } catch (error: FirebaseAuthInvalidUserException) {
                            validateExceptionServerResponse(errorServerResponse = error.errorCode)
                        } catch (error: Exception) {
                            _stateFlow.emit(OtpVerificationStates.ValidateErrorServerResponse(error.localizedMessage
                                ?: "Something went wrong"))
                        }

                    } else {
                        Log.d("MainActivity", "TEST ERROR")
                    }
                }
            }
        }
    }

    private fun validateUserOtp(
        userOtpEnter: String,
    ): Boolean {
        Log.d("MainActivity", "User Otp Entered $userOtpEnter")
        if (userOtpEnter.isEmpty()) {
            Log.d("MainActivity", "EMPTY BAY SORRY")
            _stateFlow.value = OtpVerificationStates.Error("Otp cannot be empty")
            return false
        }

        if (userOtpEnter.length in 1..5) {
            Log.d("MainActivity", "TEST")
            _stateFlow.value =
                OtpVerificationStates.Error("Ops your otp input should not be less than 6")
            return false
        }


        return userOtpEnter.isNotEmpty() && userOtpEnter.length == 6


    }
//       return
//        return if (errorList.isEmpty()) null else errorList


    private fun validateExceptionServerResponse(errorServerResponse: String) {
        viewModelScope.launch {
            when (errorServerResponse) {
                "ERROR_USER_NOT_FOUND" -> _stateFlow.emit(OtpVerificationStates.ValidateErrorServerResponse(
                    "Sorry but your phone number was not found"))

                "ERROR_USER_TOKEN_EXPIRED" -> _stateFlow.emit(OtpVerificationStates.ValidateErrorServerResponse(
                    "Sorry but your OTP token was expired please click re send button to generate another OTP in your phone number"))

                "ERROR_INVALID_USER_TOKEN" -> _stateFlow.emit(OtpVerificationStates.ValidateErrorServerResponse(
                    "Sorry but your OTP input was incorrect, Please double check it properly in order your account will be successfully created"))

            }
        }
    }


}