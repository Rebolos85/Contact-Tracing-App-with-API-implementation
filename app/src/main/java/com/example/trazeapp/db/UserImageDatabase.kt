package com.example.trazeapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.trazeapp.dao.ImageDao
import com.example.trazeapp.db.entity.UserImage
import com.example.trazeapp.imageconverters.Converters

@Database(entities = [UserImage::class],
    version = 1)
@TypeConverters(Converters::class)
abstract class UserImageDatabase : RoomDatabase() {
    abstract fun getImageDao(): ImageDao
}