package com.example.trazeapp.service.impl

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import javax.inject.Inject

class GalleyChoicesIntentImpl @Inject constructor() : ActivityResultContract<Intent?, Uri?>() {
    override fun createIntent(context: Context, input: Intent?): Intent {
        return input!!
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        var uriPhoto: Uri? = null
        when (resultCode) {
            Activity.RESULT_OK -> {
                uriPhoto = intent?.data

            }
        }
        return uriPhoto
    }
}