package com.example.trazeapp.repository

import android.net.Uri
import androidx.fragment.app.Fragment
import com.example.trazeapp.data.model.Name
import com.example.trazeapp.data.model.SingleLiveEvent
import com.example.trazeapp.db.entity.UserImage
import com.google.firebase.firestore.QuerySnapshot


/**
 * AKOA SIYA GE INTERFACE IN ORDER REUSABLE SIYA LET SAY WE HAVE
 * AN API BACKEND SO WE COULD IMPLEMENT THIS ONE IN ORDER
 * KAY DAPAT NAKA DEPEDENCY INVERSION NA SIYA WHICH MEANS
 * WE SHOULD DEPEND ON ABSTRACT OR INTERFACE NOT ON CONCRETION
 */
interface UserRepository {
    suspend fun login(email: String, password: String)
    suspend fun registerToFireStore(
        email: String,
        name: Name,
        phoneNumber: String,
        city: String,
        uriPathImage: String,
    )

    suspend fun registerToEmailAuth(userEmail: String, userPassword: String)
    suspend fun verifyOtpSentToPhone(verificationOtp: String, userEnterOtp: String)
    suspend fun logout()
    suspend fun uploadPicture(uploadCropImageUri: Uri?)
    suspend fun insertImageToRoom(userImage: UserImage)
    fun getPhoneAuthResponse(): SingleLiveEvent<String?>
    fun getUserOtp(): SingleLiveEvent<String?>
    suspend fun requestPhoneNumber(phoneNumber: String, fragment: Fragment)
    suspend fun checkUserPhoneNumberExist(phoneNumberInput: String): QuerySnapshot
    suspend fun getPhoneUserId()
    fun getPhoneNumberMessage(): SingleLiveEvent<String?>

    fun getPhoneAuthMessage():SingleLiveEvent<String?>
}




