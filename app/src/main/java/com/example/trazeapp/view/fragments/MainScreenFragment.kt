package com.example.trazeapp.view.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.trazeapp.R
import com.example.trazeapp.databinding.FragmentMainScreenBinding
import com.example.trazeapp.other.Constants.appRequestPermission
import com.example.trazeapp.util.multiplePermission
import com.example.trazeapp.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainScreenFragment : Fragment() {


    private var _binding: FragmentMainScreenBinding? = null


    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentMainScreenBinding.inflate(inflater, container, false)

        return binding.root


    }

    private fun showAppRequestPermission() {
        val showAppPermission = multiplePermission { permissions ->
            val granted = permissions.entries.all {
                it.value
            }
            if (granted) {
                requireContext().toast("Thank you for accepting of all app request you may know proceed")
            } else {
                showDialogFragment()
            }

        }
        launchRequestByApp(appPermission = showAppPermission)

    }


    private fun launchRequestByApp(appPermission: ActivityResultLauncher<Array<String>>) {
        appPermission.launch(appRequestPermission)
    }

    // kani nga fragment kani
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showAppRequestPermission()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.apply {
            btnLogin.setOnClickListener {
                val actionToLogin =
                    MainScreenFragmentDirections.actionMainScreenFragmentToLoginFragment()
                findNavController().navigate(actionToLogin)
            }

            registerButton.setOnClickListener {
                val actionToChoicesFragment =
                    MainScreenFragmentDirections.actionMainScreenFragmentToHomeFragment()

                findNavController().safeNavigate(actionToChoicesFragment)
            }

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDialogFragment() {
        findNavController().navigate(R.id.action_global_permissionFragmentDialog)
    }

    fun NavController.safeNavigate(direction: NavDirections) {
        currentDestination?.getAction(direction.actionId)?.run { navigate(direction) }
    }
}













