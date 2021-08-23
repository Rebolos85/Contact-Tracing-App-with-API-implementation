package com.example.trazeapp.data.model

import android.net.Uri

data class IndividualModel(
    val username: String,
    var password: String,
    var confirmPassword: String,
    val firstName: String,
    val lastName: String,
    val middleName: String?,
    val suffixName: String?,
    val phoneNumber: String,
    val email: String,
    val city: String,
    val captureImageUri: Uri?,
    var unfinishedPhotoUri: Uri?,
)



