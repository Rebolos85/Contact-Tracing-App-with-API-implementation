package com.example.trazeapp.db.entity

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_account")
data class UserImage(
    @ColumnInfo(
        typeAffinity = ColumnInfo.BLOB,
    )
    var imageBitmap: Bitmap? = null,

    var timeStamp: Long? = null,
) {
    @PrimaryKey
    @ColumnInfo(name = "image_upload_id")
    var imageId: Int? = null
}