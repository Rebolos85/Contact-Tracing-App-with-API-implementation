package com.example.trazeapp.view.fragments

import android.content.DialogInterface.BUTTON_NEGATIVE
import android.content.DialogInterface.BUTTON_POSITIVE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.ImageLoader
import coil.request.ImageRequest
import com.example.trazeapp.R
import com.example.trazeapp.data.model.*
import com.example.trazeapp.databinding.FragmentRegisterBinding
import com.example.trazeapp.other.UriUploadImageCatcher
import com.example.trazeapp.repository.impl.FirebasePhoneAuthProvider
import com.example.trazeapp.repository.source.PhoneAuthSource
import com.example.trazeapp.service.CropImageSource
import com.example.trazeapp.service.RegisterDialogSource
import com.example.trazeapp.util.error.InputErrorTarget
import com.example.trazeapp.util.error.PhotoStates
import com.example.trazeapp.util.imageutils.ImageUtils.getTemporaryUri
import com.example.trazeapp.util.states.CredentialStates
import com.example.trazeapp.util.states.PhotoCurrentState
import com.example.trazeapp.util.toast
import com.example.trazeapp.viewmodel.RegisterFragmentViewModel
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showDrawable
import com.github.razir.progressbutton.showProgress
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class RegisterFragment : Fragment() {


    private var _binding: FragmentRegisterBinding? = null

    // para sure nga dili null ang binding
    private val binding get() = _binding!!


    // ma initialize rani once na tawagon and then ma save dayon siya and pang one time ra siya ma execute
    private val viewModel: RegisterFragmentViewModel by viewModels()

    @Inject
    lateinit var cropImageSource: CropImageSource

    @Inject
    lateinit var registerDialogSource: RegisterDialogSource

    private var latestTmpUri: Uri? = null

    @Inject
    lateinit var activityResultContract: ActivityResultContract<Intent?, Uri?>
    var cropImageUri: Uri? = null

    @Inject
    lateinit var phoneAuthSource: PhoneAuthSource

    @Inject
    lateinit var firebasePhoneAuthProvider: FirebasePhoneAuthProvider

    @Inject
    lateinit var cropImageActivityContract: ActivityResultContract<Uri?, CropImage.ActivityResult?>

    private lateinit var takePhotoResultLauncher: ActivityResultLauncher<Intent?>

    private lateinit var resultCropImage: ActivityResultLauncher<Uri?>
    private lateinit var resizeImage: Bitmap
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        resultCropImage = registerForActivityResult(cropImageActivityContract) { cropImageResult ->
            cropImageResult?.let { cropImageFileUri ->
                cropImageUri = cropImageFileUri.uri
                cropImageUri?.let { finalCropImageResult ->
                    cropImageSource.setImage(finalCropImageResult,
                        this@RegisterFragment,
                        binding.imgTakePicture)
                }
            }

        }


        takePhotoResultLauncher = registerForActivityResult(activityResultContract) {
            latestTmpUri?.let { result ->
                resultCropImage.launch(result)


            }
        }



        return binding.root
    }


    private fun Button.hideLoading() {
        hideProgress(R.string.label_register)
    }

    private fun Button.showLoading() {
        showProgress {
            buttonTextRes = R.string.loading
            progressColor = Color.WHITE
        }
    }

    private fun Button.showSucessState() {
        val animatedDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.check_icon)
//Defined bounds are required for your drawable
        animatedDrawable?.setBounds(0, 0, 40, 40)
        animatedDrawable?.let {
            showDrawable(it) {
                buttonTextRes = R.string.Done
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listOfCountries = resources.getStringArray(R.array.list_of_towns)
        val arrayAdapter =
            ArrayAdapter(requireContext(), R.layout.list_item, listOfCountries)


        bindProgressButton(binding.registerButton)
        binding.apply {

            cityInput.setAdapter(arrayAdapter)




            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                viewModel.photoStateFlow.collect { errorPhotoStates ->
                    when (errorPhotoStates) {
                        is PhotoCurrentState.Idle -> processPhotoError(emptyList())
                        is PhotoCurrentState.PhotoErrorList -> errorPhotoStates.errorListOfPhotoState?.let { eachPhotoError ->
                            processPhotoError(eachPhotoError)
                        }


                        is PhotoCurrentState.SuccessPhotoState -> requireContext().toast(
                            errorPhotoStates.message)
                        is PhotoCurrentState.SetBitmapPhoto -> resizeImage = errorPhotoStates.bitmap
                    }
                }
            }


            registerButton.setOnClickListener {

//                phoneAuthSource.requestPhoneAuthentication(phoneNumber = countryCodeWithPhoneNumber,
//                    this@RegisterFragment)

                // pag buhi pa ang view sa fragment kay mas taas og kinabuhi ang fragment
                viewLifecycleOwner.lifecycleScope.launchWhenCreated {

                    binding.apply {
                        val userName = usernameInput.text?.toString()?.trim() ?: ""
                        val password = passwordInput.text?.toString()?.trim() ?: ""
                        val confirmPassword = confirmPasswordInput.text?.toString()?.trim() ?: ""
                        val firstName = firstnameInput.text?.toString()?.trim() ?: ""
                        val lastName = lastnameInput.text?.toString()?.trim() ?: ""
                        val middleName = middleNameInput.text?.toString()?.trim() ?: ""
                        val suffixName = suffixNameInput.text?.toString()?.trim() ?: ""
                        val contactNumber = contactNumberInput.unMaskedText?.trim() ?: ""
                        val withCountryCodePhoneNumber =
                            "+63" + contactNumberInput.unMaskedText?.trim() ?: ""
                        val email = emailInput.text?.toString()?.trim() ?: ""
                        val city = cityInput.text?.toString()?.trim() ?: ""

                        val model = IndividualModel(
                            username = userName,
                            password = password,
                            confirmPassword = confirmPassword,
                            firstName = firstName,
                            lastName = lastName,
                            middleName = middleName,
                            suffixName = suffixName,
                            phoneNumber = withCountryCodePhoneNumber,
                            email = email,
                            city = city,
                            latestTmpUri,
                            cropImageUri,
                        )

                        viewModel.register(model, this@RegisterFragment)
//                        viewModel.sentOtpToPhoneNumber(model,this@RegisterFragment)


                        updateUserCredentials(
                            userEmail = email, firstName = firstName, lastName = lastName,
                            suffixName = suffixName,
                            middleName = middleName,
                            phoneNumber = withCountryCodePhoneNumber,
                            city = city


                        )

                    }


                }
            }



            imgTakePicture.setOnClickListener {
                listenerForContinueDialog()
            }

            changeType.setOnClickListener {
                showChoicesFragment()
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun processErrors(errorList: List<InputError>) {
        binding.apply {
            if (errorList.isNullOrEmpty()) {
                firstNameLayout.error = null
                passwordLayout.error = null
                confirmPasswordLayout.error = null
                usernameLayout.error = null
                middleNameLayout.error = null
                cityLayout.error = null
                suffixLayout.error = null
                lastNameLayout.error = null
                contactNumberLayout.error = null
                emailLayout.error = null

                // pero naa na dayon value ang geluha sa errorFlow naay errror
            } else {
//                    binding.registerButton.hideProgress(R.string.label_register)
                errorList.forEach { errorOccurred ->

                    val errorLayout = when (errorOccurred.target) {
                        // MURA NI SIYAG ADDRESS KUNG AHA IPADALA ANG ERROR
                        InputErrorTarget.USERNAME -> usernameLayout
                        InputErrorTarget.PASSWORD -> passwordLayout
                        InputErrorTarget.CONFIRM_PASSWORD -> confirmPasswordLayout
                        InputErrorTarget.FIRST_NAME -> firstNameLayout
                        InputErrorTarget.MIDDLE_NAME -> middleNameLayout
                        InputErrorTarget.LAST_NAME -> lastNameLayout
                        InputErrorTarget.EXTENSION_NAME -> suffixLayout
                        InputErrorTarget.CONTACT_NUMBER -> contactNumberLayout
                        InputErrorTarget.EMAIL -> emailLayout
                        InputErrorTarget.CITY -> cityLayout

                    }

                    errorLayout.error = errorOccurred.message
                }
            }
        }
    }


    private fun listenerForContinueDialog() {
        with(registerDialogSource) {
            showInformationDialog(requireContext()
            ) { infomartionDialog, which ->
                with(infomartionDialog) {
                    when (which) {
                        BUTTON_POSITIVE -> {
                            cancel()
                            determineUserIntentChoices()
                        }
                    }
                }
            }
        }

    }

    private fun determineUserIntentChoices() {
        with(registerDialogSource) {
            showChoicesCameraDialog(requireContext()) { choicesDialog, userSelected ->
                with(choicesDialog) {
                    when (userSelected) {
                        BUTTON_POSITIVE -> {
                            cancel()
                            showCaptureIntent()
                        }

                    }
                }
            }
        }

    }

    private fun showEmptyPhotoDescriptionDialog(message: Int) {
        with(registerDialogSource) {
            showPhotoInformationEmptyDialog(requireContext(),
                message) { emptyDialogChoices, userSelected ->
                with(emptyDialogChoices) {
                    when (userSelected) {
                        BUTTON_POSITIVE -> {
                            cancel()
                            determineUserIntentChoices()
                        }
                        BUTTON_NEGATIVE -> cancel()
                    }
                }
            }
        }
    }

    private fun showUnSucessfulCropImageDialog(message: Int) {
        with(registerDialogSource) {
            showUnsuccessfulCropImageDialogMessage(requireContext(),
                message) { unSuccessfulDialog, choices ->
                with(unSuccessfulDialog) {
                    when (choices) {
                        BUTTON_POSITIVE -> {
                            cancel()
                            determineUserIntentChoices()
                        }
                        BUTTON_NEGATIVE -> cancel()
                    }
                }
            }

        }
    }

    private fun showCaptureIntent(): Intent? {
        var captureIntent: Intent? = null
        val isDevicesSupportCamera: Boolean =
            requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)


        if (isDevicesSupportCamera) {
            getTemporaryUri(requireContext().applicationContext)?.let { uri ->
                latestTmpUri = uri


                captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                    putExtra(MediaStore.EXTRA_OUTPUT, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    takePhotoResultLauncher.launch(this)

                }
            }


        }
        return captureIntent
    }

    private fun processPhotoError(photoErrorList: List<PhotoError>) {

        photoErrorList.forEach { eachPhotoError ->
            when (eachPhotoError.photoStates) {
                PhotoStates.CANCEL_UNCROP_PHOTO -> showUnSucessfulCropImageDialog(message = eachPhotoError.message)
                PhotoStates.EMPTY_UNCROP_PHOTO -> {
                    showEmptyPhotoDescriptionDialog(message = eachPhotoError.message)
                }

            }

        }
    }

    private fun showChoicesFragment() {
        val actionToChoicesFragment =
            RegisterFragmentDirections.actionRegisterFragmentToChoicesFragment()
        findNavController().navigate(actionToChoicesFragment)
    }

    private fun showOtpVerificationFragment(
        registerAsIndividual: RegisterAsIndividual,
        verificationId: String,
    ) {


        val actionToOtpFragment =
            RegisterFragmentDirections.actionRegisterFragmentToOtpFragment(
                verificationOneTimePassword = verificationId,
                registerAsIndividual)
        findNavController().navigate(actionToOtpFragment)
    }


    private fun updateUserCredentials(
        userEmail: String,
        firstName: String,
        lastName: String,
        suffixName: String,
        middleName: String,
        phoneNumber: String,
        city: String,
    ) {
        binding.apply {
            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                viewModel.stateFlow.collect { credentialStates ->
                    registerButton.hideLoading()
                    when (credentialStates) {
                        //
                        is CredentialStates.Loading -> {
                            registerButton.showLoading()
                        }

                        is CredentialStates.ValidServerErrorResponse -> {
                            firstNameLayout.error = null
                            passwordLayout.error = null
                            confirmPasswordLayout.error = null
                            usernameLayout.error = null
                            middleNameLayout.error = null
                            cityLayout.error = null
                            suffixLayout.error = null
                            lastNameLayout.error = null
                            contactNumberLayout.error = null
                            emailLayout.error = null
                            requireContext().toast(credentialStates.message)
                        }
                        is CredentialStates.ValidateError -> credentialStates.inputErrors?.let { eachCredentialEmpty ->
                            requireContext().toast("HOOY ERROR ")
                            processErrors(eachCredentialEmpty)


                        }
                        CredentialStates.SuccessAuth -> {
                            requireContext().toast(R.string.label_description_phone_authentication_response)
                            registerButton.showSucessState()
                            processErrors(emptyList())

                        }

                    }

                }
            }


            viewModel.getUserPhoneOtp().observe(viewLifecycleOwner) { verificationId ->
                val registerModelIndividual =
                    RegisterAsIndividual(
                        email = userEmail,
                        basicUserCredentials = Name(
                            first = firstName,
                            last = lastName,
                            suffix = suffixName,
                            middle = middleName),
                        city = city,
                        phoneNumber = phoneNumber,
                        resizeImageBitmap = resizeImage)

                verificationId?.let {
                    showOtpVerificationFragment(registerAsIndividual = registerModelIndividual,
                        verificationId)
                } ?: run {

                }
            }

            viewModel.getPhoneAuthResponse().observe(viewLifecycleOwner) { phoneAuthResponse ->
                phoneAuthResponse?.let {

                } ?: run {
                    registerButton.showSucessState()
                }
            }
        }

    }


}


























