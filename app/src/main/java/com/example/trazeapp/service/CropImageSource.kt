package com.example.trazeapp.service

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.theartofdev.edmodo.cropper.CropImageView

interface CropImageSource {

    fun launchImageCrop(context: Context, unCropImageUri: Uri): Intent

    fun setImage(individualUri: Uri, fragment: Fragment, imageView: ImageView): Boolean?

    fun getCropImageResult(context: Context): Uri?


}