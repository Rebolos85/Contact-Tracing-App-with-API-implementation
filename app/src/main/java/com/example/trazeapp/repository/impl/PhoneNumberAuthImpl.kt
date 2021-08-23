package com.example.trazeapp.repository.impl

import android.util.Log
import androidx.fragment.app.Fragment
import com.example.trazeapp.repository.source.PhoneAuthSource
import com.google.firebase.auth.*
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PhoneNumberAuthImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val phoneAuthProvider: FirebasePhoneAuthProvider,
) :
    PhoneAuthSource {
    // kani ang mag send og otp nga sa phone number
    override suspend fun requestPhoneAuthentication(phoneNumber: String, fragment: Fragment) {
        val phoneAuthOption = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(fragment.requireActivity())
            .setCallbacks(phoneAuthProvider.callback)

            .build()
        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOption)
    }

    override suspend fun verifyOtp(
        verificationOtpGenerated: String,
        userEnterOtp: String,
    ): String {
        val phoneCredential =
            PhoneAuthProvider.getCredential(verificationOtpGenerated, userEnterOtp)
        return firebaseAuth.signInWithCredential(phoneCredential).await().user!!.uid

    }

    override fun verifyOtpAgain(phoneCrendentials: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(phoneCrendentials)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("MainActivity", "SUCCESS")
                } else {
                    if (it.exception is FirebaseAuthInvalidCredentialsException) {
                        Log.d("MainActivity", "FAILED OTP")
                    }
                }
            }
    }


}
