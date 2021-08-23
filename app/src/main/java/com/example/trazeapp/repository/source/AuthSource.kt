package com.example.trazeapp.repository.source

import androidx.fragment.app.Fragment
import kotlinx.coroutines.flow.Flow

interface AuthSource {
    suspend fun login(email: String, password: String)
    suspend fun register(email: String, password: String): String


    fun hasUserFlow(): Flow<Boolean>
    fun hasUser(): Boolean
    suspend fun deleteCurrentUser()
    suspend fun logout()

}