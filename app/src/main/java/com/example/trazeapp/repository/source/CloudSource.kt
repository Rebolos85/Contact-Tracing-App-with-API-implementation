package com.example.trazeapp.repository.source

import android.net.Uri


interface CloudSource {

    suspend fun uploadImage(
        cropImage: Uri?,
    )


}