package com.example.trazeapp.util.states

import android.graphics.Bitmap
import com.example.trazeapp.data.model.PhotoError

sealed class PhotoCurrentState {
    object Idle : PhotoCurrentState()

    data class PhotoErrorList(val errorListOfPhotoState: MutableList<PhotoError>?) :
        PhotoCurrentState()
    data class SuccessPhotoState(val message: Int) : PhotoCurrentState()
    data class SetBitmapPhoto(val bitmap: Bitmap) : PhotoCurrentState()
}
