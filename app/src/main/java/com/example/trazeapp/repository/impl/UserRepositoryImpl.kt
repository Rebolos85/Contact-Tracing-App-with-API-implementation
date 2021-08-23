package com.example.trazeapp.repository.impl

import android.net.Uri
import androidx.fragment.app.Fragment
import com.example.trazeapp.dao.ImageDao
import com.example.trazeapp.data.model.Name
import com.example.trazeapp.data.model.SingleLiveEvent
import com.example.trazeapp.data.model.User
import com.example.trazeapp.db.entity.UserImage
import com.example.trazeapp.repository.UserRepository
import com.example.trazeapp.repository.source.AuthSource
import com.example.trazeapp.repository.source.ProfileSource
import com.example.trazeapp.repository.source.CloudSource
import com.example.trazeapp.repository.source.PhoneAuthSource
import com.example.trazeapp.util.retry
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(
    private val authSource: AuthSource,
    private val profileSource: ProfileSource,
    private val cloudSource: CloudSource,
    private val phoneAuthSource: PhoneAuthSource,
    private val phoneAuthProvider: FirebasePhoneAuthProvider,
    private val imageDao: ImageDao,
) : UserRepository {

    companion object {
        var emailId: String? = null
        var phoneNumberId: String? = null
    }


    override suspend fun login(email: String, password: String) {

        authSource.login(email = email, password = password)


    }

    override suspend fun registerToEmailAuth(userEmail: String, userPassword: String) {
        emailId = authSource.register(email = userEmail, password = userPassword)
    }

    override suspend fun registerToFireStore(
        email: String,
        name: Name,
        phoneNumber: String,
        city: String,
        uriPathImage: String,
    ) {
        // nag register ka sa firebase nga auth
        // i  retry nato kay para dili mo failed
        retry(initialDelay = 100, maxDelay = 1000) {
            emailId?.let { userRegisterId ->
                profileSource.createUserRecord(
                    User(
                        emailUserId = userRegisterId,
                        email = email,
                        name = name,
                        city = city,
                        phoneNumber = phoneNumber,
                        uriPathImageUpload = uriPathImage

                    )
                )
            }
        }
    }


    override suspend fun verifyOtpSentToPhone(verificationOtp: String, userEnterOtp: String) {
        phoneNumberId =  phoneAuthSource.verifyOtp(verificationOtpGenerated = verificationOtp,
            userEnterOtp = userEnterOtp)
    }


    override suspend fun logout() {
        authSource.logout()
    }

    override suspend fun requestPhoneNumber(phoneNumber: String, fragment: Fragment) {
        phoneAuthSource.requestPhoneAuthentication(phoneNumber = phoneNumber, fragment)
    }

//    override suspend fun requestPhoneNumber(phoneNumber: String, fragment: Fragment) {
//        phoneAuthSource.requestPhoneAuthentication(phoneNumber = phoneNumber, fragment = fragment)
//    }

    override suspend fun uploadPicture(uploadCropImageUri: Uri?) {
        uploadCropImageUri?.let { cropImage ->
            cloudSource.uploadImage(cropImage)
        }
    }

    override suspend fun insertImageToRoom(userImage: UserImage) =
        imageDao.insertImage(userImage = userImage)

    override fun getPhoneAuthResponse(): SingleLiveEvent<String?> =
        phoneAuthProvider.duplicatePhoneNumber


    override fun getUserOtp(): SingleLiveEvent<String?> = phoneAuthProvider.verificationIdLiveEvent


}

