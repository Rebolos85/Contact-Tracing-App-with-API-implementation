package com.example.trazeapp.viewmodel


import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trazeapp.R
import com.example.trazeapp.adapter.bitmapresolver.BitmapResolver
import com.example.trazeapp.data.model.IndividualModel
import com.example.trazeapp.data.model.InputError
import com.example.trazeapp.data.model.PhotoError
import com.example.trazeapp.db.entity.UserImage

import com.example.trazeapp.repository.UserRepository
import com.example.trazeapp.util.error.InputErrorTarget
import com.example.trazeapp.util.error.PhotoStates
import com.example.trazeapp.util.states.CredentialStates
import com.example.trazeapp.util.states.PhotoCurrentState
import com.example.trazeapp.util.validator.Validators.isValidName
import com.example.trazeapp.util.validator.Validators.isValidPassword
import com.example.trazeapp.view.fragments.RegisterFragment
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.imageresizer.ImageResizer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterFragmentViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val bitmapResolver: BitmapResolver,
    private val imageResizer: ImageResizer,
) : ViewModel() {

    private val _stateFlow = MutableStateFlow<CredentialStates>(CredentialStates.Idle)
    val stateFlow: Flow<CredentialStates> get() = _stateFlow

    private val _photoStateFlow = MutableStateFlow<PhotoCurrentState>(PhotoCurrentState.Idle)
    val photoStateFlow: Flow<PhotoCurrentState> get() = _photoStateFlow

    fun register(
        individualCredentials: IndividualModel,
        registerFragment: RegisterFragment,


        ) {
        val countryCodeWithPhoneNumber = "+63" + individualCredentials.phoneNumber
        viewModelScope.launch {
            _stateFlow.emit(CredentialStates.Idle)
            with(individualCredentials) {
                val validateInput = validateInput(individualCredentials)
                val photoStates = validateUserPhoto(model = individualCredentials, validateInput)
                with(userRepository) {
                    if (validateInput.isNullOrEmpty() && photoStates.isNullOrEmpty()) {

                        // para sure ma executed ni siya una ayha ang ubos
                        _stateFlow.emit(CredentialStates.Loading)
                        try {
//                            registerToEmailAuth(userEmail = email,
//                                userPassword = password)

                            requestPhoneNumber(phoneNumber = phoneNumber, registerFragment)
                            _stateFlow.emit(CredentialStates.SuccessAuth)

                        } catch (error: FirebaseAuthInvalidCredentialsException) {

                            _stateFlow.emit(
                                CredentialStates.ValidServerErrorResponse("Please input a proper email address in order you for your registration")
                            )

                        } catch (error: FirebaseAuthUserCollisionException) {
                            when (error.errorCode) {
                                "ERROR_EMAIL_ALREADY_IN_USE" -> _stateFlow.emit(CredentialStates.ValidServerErrorResponse(
                                    "Sorry, the email you entered is already use in different account please enter another email in order you can proceed."))

                                "ERROR_CREDENTIAL_ALREADY_IN_USE" -> _stateFlow.emit(
                                    CredentialStates.ValidServerErrorResponse(
                                        "Sorry, but contact number you entered is already in different account please enter another phone number in order you can proceed"))
                            }

                        } catch (error: FirebaseAuthWeakPasswordException) {
                            _stateFlow.emit(CredentialStates.ValidServerErrorResponse("Please enter a password that has at least eight characters with at least one Special Characters, One Digit, One Capital letter and Lowercase letter"))

                        } catch (manyRequest: FirebaseTooManyRequestsException) {
                            _stateFlow.emit(CredentialStates.ValidServerErrorResponse("Sorry but you're restricted for temporary due to you have so many request which it can result to unusual activity, Please try again later"))
                        } catch (error: Exception) {
                            _stateFlow.emit(CredentialStates.ValidServerErrorResponse("Please check your internet connection"))
                        }
                    } else {
                        _stateFlow.emit(CredentialStates.ValidateError(validateInput))
                        _photoStateFlow.emit(PhotoCurrentState.PhotoErrorList(photoStates))
                    }
                }
            }
        }

    }


    private fun validateInput(
        individualRegister: IndividualModel,
    ): MutableList<InputError>? {
        val errorList = mutableListOf<InputError>()
        with(individualRegister) {

            if (username.isBlank()) {
                errorList.add(
                    InputError(
                        InputErrorTarget.USERNAME,
                        "Username cannot be empty"
                    )
                )
            }

            when {
                password.isEmpty() -> {
                    errorList.add(
                        InputError(
                            InputErrorTarget.PASSWORD,
                            "Password cannot be empty"
                        )
                    )
                }
                isValidPassword(password) -> {

                }

                else -> {
                    errorList.add(
                        InputError(
                            InputErrorTarget.PASSWORD,
                            "Please follow the required password"
                        )
                    )

                }
            }

            when {
                confirmPassword.isEmpty() -> {
                    errorList.add(
                        InputError(
                            InputErrorTarget.CONFIRM_PASSWORD,
                            "Confirm password cannot be empty"
                        )
                    )
                }
                isValidPassword(confirmPassword) && password == confirmPassword -> {
                }

                else -> {
                    errorList.add(
                        InputError(
                            InputErrorTarget.CONFIRM_PASSWORD,
                            "Password doesn't match"
                        )
                    )

                }

            }
            if (firstName.isNotBlank() && isValidName(firstName)) {

            } else if (firstName.isEmpty()) {
                errorList.add(InputError(InputErrorTarget.FIRST_NAME,
                    "Firstname cannot be empty"))
            } else {
                errorList.add(
                    InputError(
                        InputErrorTarget.FIRST_NAME,
                        "Firstname should be alphabet letter only"
                    )
                )
            }
            if (lastName.isNotEmpty() && isValidName(lastName)) {

            } else if (lastName.isEmpty()) {
                errorList.add(InputError(InputErrorTarget.LAST_NAME,
                    "Lastname cannot be empty"))
            } else {
                errorList.add(
                    InputError(
                        InputErrorTarget.LAST_NAME,
                        "Firstname should be alphabet letter only"
                    )
                )
            }
            if (city.isEmpty()) {
                errorList.add(InputError(InputErrorTarget.CITY, "City cannot be empty"))
            }
            if (phoneNumber.isEmpty()) {
                errorList.add(InputError(InputErrorTarget.CONTACT_NUMBER,
                    "Contact number cannot be empty"))
            }


            return if (errorList.isNullOrEmpty()) null else errorList
        }

    }

    private fun validateUserPhoto(
        model: IndividualModel,
        validateInput: MutableList<InputError>?,
    ): MutableList<PhotoError>? {
        val errorPhotoList = mutableListOf<PhotoError>()
        with(model) {


            when {
                validateInput.isNullOrEmpty() && captureImageUri == null -> {
                    errorPhotoList.add(PhotoError(PhotoStates.EMPTY_UNCROP_PHOTO,
                        R.string.label_photo_empty))
                }
                validateInput.isNullOrEmpty() && unfinishedPhotoUri == null -> {
                    Log.d("MainActivity", "GANA NA BA LITSEHA OY")
                    errorPhotoList.add(PhotoError(PhotoStates.CANCEL_UNCROP_PHOTO,
                        R.string.label_unfinished_cropping_image_description))
                }
                validateInput.isNullOrEmpty() && unfinishedPhotoUri != null -> {
                    var bitmap: Bitmap? = null
                    unfinishedPhotoUri?.let { cropImageUri ->

                        bitmap = bitmapResolver.getBitmapOfCapturedImage(cropImageUri)


                    }
                    bitmap?.let { bitmapForResizedImage ->
                        setResizedImageBitmap(bitmap = bitmapForResizedImage)
                        Log.d("MainActivity", "Success resizedImage")
                    }

                }
                else -> errorPhotoList == emptyList<PhotoError>()

            }
        }




        return if (errorPhotoList.isEmpty()) null else errorPhotoList

    }

    private fun uploadImage(captureImageFinished: Uri?) {
        viewModelScope.launch {
            captureImageFinished?.let { cropImageFinishedUri ->
                userRepository.uploadPicture(cropImageFinishedUri)
            }

        }
    }


    fun getUserPhoneOtp() = userRepository.getUserOtp()

    private fun setResizedImageBitmap(bitmap: Bitmap) {

        val bitmapForResizeImage = imageResizer.resizeBitmap(bitmap, 150)
//        insert(bitmapForResizeImage)
        _photoStateFlow.value = PhotoCurrentState.SetBitmapPhoto(bitmap)
    }

//    private fun insert(bitmapOfResizedImage: Bitmap) {
//        val timeStamp = System.currentTimeMillis()
//        val userImage = UserImage(bitmapOfResizedImage, timeStamp)
//        viewModelScope.launch(Dispatchers.IO) {
//            userRepository.insertImageToRoom(userImage)
//            _photoStateFlow.emit(PhotoCurrentState.SuccessPhotoState(R.string.Success_photo_upload_description))
//
//        }
//    }

    fun getPhoneAuthResponse() = userRepository.getPhoneAuthResponse()


}





