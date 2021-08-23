package com.example.trazeapp.service.impl

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.trazeapp.service.CropImageSource
import com.example.trazeapp.util.toast
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import javax.inject.Inject

class IndividualRegisterCropImp @Inject constructor() : CropImageSource {
    override fun launchImageCrop(
        context: Context,
        unCropImageUri: Uri,
    ): Intent {
        return CropImage.activity(unCropImageUri)
            .setAspectRatio(1920, 1920)
            .setActivityTitle("Crop App")
            .setOutputCompressQuality(100)
            .setMinCropResultSize(1680, 1440)
            .setMaxCropResultSize(1920, 1700)
            .setAllowRotation(false)
            .setFixAspectRatio(true)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setOutputUri(unCropImageUri)
            .getIntent(context)


    }

    override fun setImage(individualUri: Uri, fragment: Fragment, imageView: ImageView): Boolean {
        if (imageView.drawable == null) {
            return false
        } else {
            Glide.with(fragment)
                .load(individualUri)

                .into(imageView)
        }
        return true
    }

    override fun getCropImageResult(context: Context): Uri? {
        return CropImage.getCaptureImageOutputUri(context)
    }


}


