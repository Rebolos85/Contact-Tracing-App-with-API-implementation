package com.example.trazeapp.provider.file

import android.content.Context
import android.os.Environment
import com.example.trazeapp.other.Constants.temporaryFileStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class FileCatcher @Inject constructor(@ApplicationContext val context: Context) {

    fun temporaryFile(): File {
        val storageDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(temporaryFileStorage, "jpg", storageDirectory)
    }
}