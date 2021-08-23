package com.example.trazeapp.util.imageutils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.example.trazeapp.BuildConfig
import com.example.trazeapp.util.toast
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object ImageUtils {
    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    fun createImageFile(context: Context): File? {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "PNG" + timeStamp + "_"
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.apply {
                createNewFile()
                deleteOnExit()
            }
        )

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = "file:" + image.absolutePath

        return image
    }

    @Throws(IllegalAccessException::class)
    fun getTemporaryUri(context: Context): Uri? {

//        val tmpFile = File.createTempFile("tmp_image_file", ".png", createImageFile()).apply {
//            createNewFile()
//            deleteOnExit()
//        }
        return createImageFile(context)?.let {

            FileProvider.getUriForFile(context.applicationContext,
                "${BuildConfig.APPLICATION_ID}.provider",
                it)

        }
    }


}
