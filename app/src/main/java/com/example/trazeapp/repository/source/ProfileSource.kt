package com.example.trazeapp.repository.source

import com.example.trazeapp.data.model.SingleLiveEvent
import com.example.trazeapp.data.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot


// TODO: Full CRUD implementation
interface ProfileSource {
    suspend fun createUserRecord(user: User)


    suspend fun getUserRecord(userId: String): User

    suspend fun getUserPhoneAuthId(phoneNumberId: String): User

    suspend fun checkUserPhoneNumberExist(phoneNumberInput: String): QuerySnapshot

    fun getPhoneExistMessage(): SingleLiveEvent<String?>
}