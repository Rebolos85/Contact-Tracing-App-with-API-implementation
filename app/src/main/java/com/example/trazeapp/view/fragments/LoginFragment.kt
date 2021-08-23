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
import com.example.trazeapp.R
import com.example.trazeapp.databinding.FragmentLoginBinding
import com.example.trazeapp.util.states.CredentialStates
import com.example.trazeapp.util.error.InputErrorTarget
import com.example.trazeapp.util.toast
import com.example.trazeapp.viewmodel.LoginFragmentViewModel
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.lang.IllegalStateException

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null

    // para sure nga dili null ang binding
    private val binding get() = _binding!!

    private val viewModel: LoginFragmentViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        viewLifecycleOwner.bindProgressButton(binding.loginButton)
        return binding.root
    }

    private fun Button.hideLoading() {
        hideProgress(R.string.label_login)
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

            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                viewModel.errorFlow.collect {
                    loginButton.hideLoading()
                    when (it) {

                        is CredentialStates.ValidServerErrorResponse -> {
                            emailLayout.error = null
//                            emailLayout.isEnabled = false
                            passwordLayout.error = null
                            requireContext().toast(it.message)
                        }

                        CredentialStates.Idle -> {
                            emailLayout.error = ""
                            passwordLayout.error = ""


                        }
                        CredentialStates.SuccessCredentials -> {
                            emailLayout.error = ""
                            passwordLayout.error = ""

                            requireContext().toast("You logged in successfully")
//                            callDashboardFragment()
                        }

                        is CredentialStates.ValidateError -> {
                            it.inputErrors?.forEach { error ->

                                val target = when (error.target) {
                                    InputErrorTarget.EMAIL -> emailLayout
                                    InputErrorTarget.PASSWORD -> passwordLayout
                                    else -> throw IllegalStateException("Error Target not found")
                                }
                                target.error = error.message
                            }
                        }
                        CredentialStates.Loading -> loginButton.showLoading()


                    }
                }

            }


            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                binding.loginButton.setOnClickListener {
//                    callDashboardFragment()
                    viewModel.login(
                        emailInput.text?.toString()?.trim() ?: "",
                        passwordInput.text?.toString()?.trim() ?: "",
                    )
                }
            }

            labelRegisterHere.setOnClickListener {
//                callRegisterFragment()
            }


        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    private fun callRegisterFragment() {
//        val loginFragmentToRegister =
//            LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
//        // this is a kotlin extension function
//        findNavController().navigate(loginFragmentToRegister)
//    }
//
//    private fun callDashboardFragment() {
//        val loginToDashboard = LoginFragmentDirections.actionLoginFragmentToDashboardFragment2()
//        findNavController().navigate(loginToDashboard)
//    }
}



