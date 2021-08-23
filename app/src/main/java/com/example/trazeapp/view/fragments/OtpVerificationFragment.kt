package com.example.trazeapp.view.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.trazeapp.R
import com.example.trazeapp.data.model.InputError
import com.example.trazeapp.databinding.FragmentVerifyOtpPhoneBinding
import com.example.trazeapp.util.states.OtpVerificationStates
import com.example.trazeapp.util.toast
import com.example.trazeapp.viewmodel.OtpVerificationViewModel
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class OtpVerificationFragment : Fragment() {
    private var _binding: FragmentVerifyOtpPhoneBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OtpVerificationViewModel by viewModels()
    private val args: OtpVerificationFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentVerifyOtpPhoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun Button.hideLoading() {
        hideProgress("Verify")
    }

    private fun Button.showLoading() {
        showProgress {
            buttonTextRes = R.string.loading
            progressColor = Color.WHITE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            bindProgressButton(verifyButton)
            val userOtpEnter: String = otpInput.text.toString().trim()
            val verificationOtp: String = args.verificationOneTimePassword
            val registerAsIndividual = args.registerCredentialsToFireStore

            verifyButton.setOnClickListener {
//                requireContext().toast("User otp entered: ${otpInput.text.toString().trim()}")
                viewModel.registerUserAccount(otpInput.text.toString().trim(),
                    verificationOtp,
                    registerAsIndividual)
            }


            viewLifecycleOwner.lifecycleScope.launchWhenCreated {

                viewModel.stateFlow.collect { userCredentials ->
                    verifyButton.hideLoading()
                    when (userCredentials) {
                        OtpVerificationStates.IDLE -> otpInput.error = null
                        OtpVerificationStates.SuccessVerification -> requireContext().toast("SUCCESS")
                        is OtpVerificationStates.ValidateErrorServerResponse -> requireContext().toast(
                            userCredentials.serverMessageResponse)
                        is OtpVerificationStates.Error -> otpInput.error = userCredentials.message
                        OtpVerificationStates.LOADING -> verifyButton.showLoading()
                    }

                }
            }
        }

    }


}