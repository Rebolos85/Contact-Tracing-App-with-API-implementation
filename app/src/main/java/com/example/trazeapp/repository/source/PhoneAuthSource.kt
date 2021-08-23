package com.example.trazeapp.repository.source

import androidx.fragment.app.Fragment
import com.google.firebase.auth.PhoneAuthCredential

interface PhoneAuthSource {


    suspend fun requestPhoneAuthentication(phoneNumber: String, fragment: Fragment)

    suspend fun verifyOtp(verificationOtpGenerated: String, userEnterOtp: String): String

     fun verifyOtpAgain(phoneCrendentials: PhoneAuthCredential)
}