package com.example.trazeapp.data.model

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


@Parcelize
data class RegisterAsIndividual(
    val email: String,
    var basicUserCredentials: @RawValue Any? = null,
    val city: String,
    val phoneNumber: String,
    val resizeImageBitmap: Bitmap,
) : Parcelable
