package com.example.trazeapp.di.module

import com.example.trazeapp.repository.UserRepository
import com.example.trazeapp.repository.impl.UserRepositoryImpl
import com.example.trazeapp.repository.source.AuthSource
import com.example.trazeapp.repository.impl.FirebaseAuthSourceImpl
import com.example.trazeapp.repository.impl.FirebaseFirestoreSourceImpl
import com.example.trazeapp.repository.impl.FirebaseStorageSourceImpl
import com.example.trazeapp.repository.source.ProfileSource
import com.example.trazeapp.repository.source.CloudSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

interface BindingsModule {

    @Binds
    @Singleton
    fun bindUserRepository(repositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    fun bindAuthSource(firebaseAuthSourceImpl: FirebaseAuthSourceImpl): AuthSource

    @Binds
    @Singleton
    fun bindProfileSource(firebaseFirestoreSourceImpl: FirebaseFirestoreSourceImpl): ProfileSource

    @Binds
    @Singleton
    fun bindFirebaseStorageSource(firebaseStorageSourceImpl: FirebaseStorageSourceImpl): CloudSource


}
