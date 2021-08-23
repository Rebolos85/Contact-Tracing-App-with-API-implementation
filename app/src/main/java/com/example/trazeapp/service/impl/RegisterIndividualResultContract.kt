package com.example.trazeapp.service.impl

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import com.example.trazeapp.service.CropImageSource
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImage.getActivityResult
import javax.inject.Inject

class RegisterIndividualResultContract @Inject constructor(
    private val cropImageSource: CropImageSource,
) : ActivityResultContract<Uri?, CropImage.ActivityResult?>() {
    override fun createIntent(context: Context, input: Uri?): Intent {
        return input?.let {
            cropImageSource.launchImageCrop(context, input)
        }!!
    }

    override fun parseResult(resultCode: Int, intent: Intent?): CropImage.ActivityResult? {
        return getActivityResult(intent)
    }
}



