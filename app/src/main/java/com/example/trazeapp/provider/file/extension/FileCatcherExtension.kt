package com.example.trazeapp.provider.file.extension

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class FileCatcherExtension @Inject constructor(@ApplicationContext val context: Context) {

    fun getFileExtension(imageUriSelected: Uri): String? {


        val cR = context.contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(imageUriSelected))
    }
}