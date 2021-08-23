package com.example.trazeapp.repository.impl

import android.net.Uri
import android.util.Log
import com.example.trazeapp.other.UriUploadImageCatcher.uriPathUploadImage
import com.example.trazeapp.repository.source.CloudSource
import com.google.firebase.storage.StorageReference
import java.util.*
import javax.inject.Inject

class FirebaseStorageSourceImpl @Inject constructor(
    private val storageFirebase: StorageReference,

    ) :
    CloudSource {


    companion object {

        val pathStorageUrl = "uploads/" + UUID.randomUUID().toString()
//        var uploadTaskUri: UploadUri? = null
//        var downloadUri: String? = null
    }

    override suspend fun uploadImage(cropImage: Uri?) {

        val storageReference =
            storageFirebase.child(pathStorageUrl)
        val uploadTask = cropImage?.let { storageReference.putFile(it)
        }
        val urlTask = uploadTask?.continueWithTask { task ->
            if (!task.isSuccessful) {
                Log.d("MainActivity", "Failed Exception ${task.exception}")
                task.exception?.let {
                    throw it
                }

            }
            storageReference.downloadUrl
        }?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result.toString()
                uriPathUploadImage = downloadUri

                Log.d("MainActivity", "Success ${task.result}")
            } else {
                Log.d("MainActivity", "Failed bay sorry")
                // Handle failures
                // ...
            }
        }


    }
}