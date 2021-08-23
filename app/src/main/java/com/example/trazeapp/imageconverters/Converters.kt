package com.example.trazeapp.imageconverters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class Converters {
    /**
     *  this function is use in orderthe bitmap
     *  will be save as byte array in order
     *  for the room to understand
     *  kay ang bitmap man gyud kay image siya
     *  so in kailangan nato siya i convert
     *  into  byte array in order nga makasabot
     *  ang
     */
    @TypeConverter // this is to tell room that this one is a type converter
    fun fromBitmap(bitmap: Bitmap): ByteArray {
        // this is use to convert the bitmap to ByteArrayOutputStream
        val outputStream = ByteArrayOutputStream()
        // this is used to compress the image and set it quality to 100
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

        return outputStream.toByteArray()
    }
    @TypeConverter // this is to tell room that this one is a type converter aron mabasa ni room ni siya nga converter
    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        // this is to interpret the byte array into bitmap
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
    }
}