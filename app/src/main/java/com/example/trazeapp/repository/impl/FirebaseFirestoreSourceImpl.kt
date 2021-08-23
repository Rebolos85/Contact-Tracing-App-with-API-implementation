package com.example.trazeapp.repository.impl


import com.example.trazeapp.data.model.User
import com.example.trazeapp.repository.source.ProfileSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class FirebaseFirestoreSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : ProfileSource {

    companion object {
        const val USERS_COLLECTION = "Users"
    }

    private val userReference = firestore.collection(USERS_COLLECTION)

    override suspend fun createUserRecord(user: User) {
        // kanang document nga paramater para userID siya like primary key
        userReference.document(user.emailUserId).set(user).await()

    }

    override suspend fun getUserRecord(
        userId: String,
    ): User {
        // kuhaon ang userID
        return userReference.document(userId).get().await().toObject()!!
    }

    override suspend fun getUserRegisterIdAuth(userId: String) {
        TODO("Not yet implemented")
    }


}
