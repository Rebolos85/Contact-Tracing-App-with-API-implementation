package com.example.trazeapp.di.module

import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import com.example.trazeapp.service.impl.GalleyChoicesIntentImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
abstract class GalleryModule {
    @Binds
    @Singleton
    abstract fun bindGalleryChoicesIntent(galleyChoicesIntentImpl: GalleyChoicesIntentImpl): ActivityResultContract<Intent?, Uri?>
}