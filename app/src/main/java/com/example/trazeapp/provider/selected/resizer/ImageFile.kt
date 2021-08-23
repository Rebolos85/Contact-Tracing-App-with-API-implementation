package com.example.trazeapp.provider.selected.resizer

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.trazeapp.provider.file.FileCatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class ImageFile @Inject constructor(private val fileCatcher: FileCatcher) {

    fun resizeTheImage(): ByteArray {
        val filePath = BitmapFactory.decodeFile(fileCatcher.temporaryFile().absolutePath)
        val byteArrayOutputStream = ByteArrayOutputStream()
        filePath.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream)

        return byteArrayOutputStream.toByteArray()
    }


}