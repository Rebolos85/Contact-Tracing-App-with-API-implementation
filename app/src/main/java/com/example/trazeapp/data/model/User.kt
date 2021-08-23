package com.example.trazeapp.data.model

import com.google.firebase.firestore.Exclude

data class User(
    @get:Exclude // para dili butang as field sa firestore
    val emailUserId: String,
    val email: String,
    val name: Name,
    val city: String,
    val phoneNumber: String,
    val uriPathImageUpload: String
)
