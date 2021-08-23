package com.example.trazeapp.di.module


import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import com.example.trazeapp.service.CropImageSource
import com.example.trazeapp.service.impl.IndividualRegisterCropImp
import com.example.trazeapp.service.impl.RegisterIndividualResultContract
import com.theartofdev.edmodo.cropper.CropImage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CropModule {
    @Singleton
    @Binds
    abstract fun bindCropIndividualRegister(individualRegisterCropImp: IndividualRegisterCropImp): CropImageSource

    @Singleton
    @Binds
    abstract fun bindCropIndividualRegisterUri(registerIndividualResultContract: RegisterIndividualResultContract): ActivityResultContract<Uri?, CropImage.ActivityResult?>

}