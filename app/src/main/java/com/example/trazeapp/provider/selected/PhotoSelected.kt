package com.example.trazeapp.provider.selected

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.trazeapp.provider.file.FileCatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class PhotoSelected @Inject constructor(
    private val fileCatcher: FileCatcher,
    @ApplicationContext val context: Context,
) {
    fun filePhotoProvider(): Uri {

        return FileProvider.getUriForFile(context,
            "com.example.trazeapp.provider.fileprovider",
            fileCatcher.temporaryFile()
        )

    }
}

