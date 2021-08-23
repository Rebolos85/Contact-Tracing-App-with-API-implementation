package com.example.trazeapp.di.module


import com.example.trazeapp.service.impl.DialogRegisterImpl
import com.example.trazeapp.service.RegisterDialogSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@InstallIn(FragmentComponent::class)
@Module
abstract class DialogModule {

    @Binds
    @FragmentScoped
    abstract fun bindDialogRegister(dialogRegisterImpl: DialogRegisterImpl): RegisterDialogSource


}