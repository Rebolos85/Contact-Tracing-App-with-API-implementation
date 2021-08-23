package com.example.trazeapp.repository.impl

import androidx.fragment.app.Fragment
import com.example.trazeapp.repository.source.AuthSource
import com.example.trazeapp.repository.source.PhoneAuthSource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : AuthSource {
    override suspend fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun register(email: String, password: String): String {
        return firebaseAuth
            .createUserWithEmailAndPassword(email, password)
            .await().user!!.uid
    }



    override suspend fun deleteCurrentUser() {
        // if ang current user is null dili na siya mag delete pa
        // tas ang await na bahala sa callback kay hinimo na siya daan
        firebaseAuth.currentUser?.delete()?.await()
    }

    override fun hasUserFlow(): Flow<Boolean> = callbackFlow {

        val authUserListener = FirebaseAuth.AuthStateListener {
            // check if naa nay user
            trySendBlocking(it.currentUser != null)
        }

        firebaseAuth.addAuthStateListener(authUserListener)
        // wala ko ka gets ani
        awaitClose {
            firebaseAuth.removeAuthStateListener(authUserListener)
        }
    }

    /*
     this is use to check nga mas mapadali ang pag check
     kung naa nabay current user kay medyo malangan man si flow
     */
    override fun hasUser(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }



}