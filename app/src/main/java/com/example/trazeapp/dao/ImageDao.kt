package com.example.trazeapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.trazeapp.db.entity.UserImage
import kotlinx.coroutines.flow.Flow
@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(userImage: UserImage)

    @Query("SELECT * FROM user_account ORDER BY imageBitmap ASC")
    fun getAllImageInAscendingOrder(): Flow<List<UserImage>>

    @Query("SELECT * FROM user_account ORDER BY timeStamp ASC")
    fun getAllImageTimeStampInAscendingOrder(): Flow<List<UserImage>>

}