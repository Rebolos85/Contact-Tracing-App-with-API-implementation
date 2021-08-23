package com.example.trazeapp.other

import android.Manifest
import android.util.Log
import com.google.firebase.auth.PhoneAuthProvider

object Constants {

    const val CAPTURE_IMAGE_REQUEST = 1
    val appRequestPermission =
        arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,

            )

    const val temporaryFileStorage = "photo"
    var phoneNumber: String? = null
    var verificationId: String? = null
    var phoneAuth: PhoneAuthProvider.ForceResendingToken? = null
    var otpEnter: String? = null
    var userCodePhoneOtp: String? = null
    fun getUserVerification() {
        verificationId?.let {
            Log.d("MainActivity", "Dili empty si verificationID")
        }
    }
}