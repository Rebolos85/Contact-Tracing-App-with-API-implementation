package com.example.trazeapp.di.module

import com.example.trazeapp.repository.impl.PhoneAuthProviderImpl
import com.example.trazeapp.repository.impl.PhoneNumberAuthImpl
import com.example.trazeapp.repository.source.PhoneAuthSource
import com.google.firebase.auth.PhoneAuthProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PhoneAuthModule {
    @Singleton
    @Binds
    abstract fun bindsPhoneNumberAuth(phoneNumberAuthImpl: PhoneNumberAuthImpl): PhoneAuthSource

    @Singleton
    @Binds
    abstract fun bindsPhoneAuthProvider(phoneAuthProviderImpl: PhoneAuthProviderImpl): PhoneAuthProvider.OnVerificationStateChangedCallbacks
}