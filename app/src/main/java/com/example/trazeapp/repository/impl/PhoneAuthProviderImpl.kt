package com.example.trazeapp.repository.impl

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.trazeapp.other.Constants.phoneAuth
import com.example.trazeapp.other.Constants.userCodePhoneOtp
import com.example.trazeapp.other.Constants.verificationId
import com.google.android.play.core.internal.s
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import javax.inject.Inject

class PhoneAuthProviderImpl @Inject constructor() :
    PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

    override fun onVerificationCompleted(p0: PhoneAuthCredential) {
        userCodePhoneOtp = p0.smsCode

    }

    override fun onVerificationFailed(p0: FirebaseException) {
        Log.d("MainActivity", "Verification Failed sorry")
    }

    override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
        super.onCodeSent(p0, p1);
        verificationId = p0
        Log.d("MainActivity", " Verification code $verificationId")
        phoneAuth = p1
    }

    override fun onCodeAutoRetrievalTimeOut(p0: String) {
        super.onCodeAutoRetrievalTimeOut(p0)
    }
}