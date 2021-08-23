package com.example.trazeapp.di.module

import android.content.Context
import androidx.room.Room
import com.example.trazeapp.db.UserImageDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomModule {

    @Provides
    @Singleton
    fun providesUserImageDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(app, UserImageDatabase::class.java, "user_account")
            .fallbackToDestructiveMigration()
            .build()


    @Provides
    @Singleton
    fun providesUserImageDao(database: UserImageDatabase) = database.getImageDao()

}