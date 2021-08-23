package com.example.trazeapp.repository.source

import android.net.Uri
import com.example.trazeapp.data.model.User


// TODO: Full CRUD implementation
interface ProfileSource {
    suspend fun createUserRecord(user: User)


    suspend fun getUserRecord(userId: String): User

    suspend fun getUserRegisterIdAuth(userId: String)

}