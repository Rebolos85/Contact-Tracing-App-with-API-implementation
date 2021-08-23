package com.example.trazeapp.repository.impl

import android.util.Log
import com.example.trazeapp.data.model.SingleLiveEvent
import com.example.trazeapp.repository.source.PhoneAuthSource
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

import javax.inject.Inject
import javax.inject.Singleton


class FirebasePhoneAuthProvider @Inject constructor() {
    val verificationIdLiveEvent = SingleLiveEvent<String?>()
    val duplicatePhoneNumber = SingleLiveEvent<String?>()

    //    val phoneAuthSuccessLiveEvent = MutableLiveData<FirebaseUser>()
//    val phoneAuthFailureLiveEvent = SingleLiveEvent<String>()
    val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
//            phoneAuthSource.verifyOtpAgain(p0)
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            if (p0 is FirebaseTooManyRequestsException) {
                duplicatePhoneNumber.value =
                    "Sorry, but your phone number you entered is already use in different user account please use another phone number. Thank you"
            }
//            Log.d("MainActivity" ,"${p0.localizedMessage}")
            Log.d("MainActivity", "Duplicate phone number : ${duplicatePhoneNumber.value}")
        }

        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(p0, p1)
            verificationIdLiveEvent.value = p0
            Log.d("MainActivity", " Test verification code : ${verificationIdLiveEvent.value}")


        }

    }
}

