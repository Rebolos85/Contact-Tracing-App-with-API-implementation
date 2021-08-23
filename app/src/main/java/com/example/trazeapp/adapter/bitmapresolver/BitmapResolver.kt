package com.example.trazeapp.adapter.bitmapresolver

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class BitmapResolver @Inject constructor(@ApplicationContext val context: Context) {

     fun getBitmapOfCapturedImage(captureImage: Uri): Bitmap {
        return when {
            Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
                context.contentResolver,
                captureImage
            )
            else -> {
                val source = ImageDecoder.createSource(context.contentResolver, captureImage)
                ImageDecoder.decodeBitmap(source)
            }
        };
    }
}