package com.example.trazeapp.view.customdialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.trazeapp.databinding.FragmentContactTracingPermissionBinding
import com.example.trazeapp.other.Constants.appRequestPermission
import com.example.trazeapp.util.multiplePermission
import com.example.trazeapp.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PermissionFragmentDialog : AppCompatDialogFragment() {
    private var _binding: FragmentContactTracingPermissionBinding? = null
    private lateinit var showDialogPermission: AlertDialog

    private val binding get() = _binding!!
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentContactTracingPermissionBinding.inflate(LayoutInflater.from(context))

        val alertDialog = AlertDialog.Builder(requireActivity())
            .setView(binding.root)
        showDialogPermission = alertDialog.create()
        eventHandlers(showDialogPermission)

        return showDialogPermission
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private val permReqLauncher = multiplePermission { eachPermission ->
        val granted = eachPermission.entries.all {
            it.value
        }
        if (granted) {
            requireContext().toast("Thank you for accepting of all app request you may know proceed")
            showDialogPermission.cancel()
        } else {
            requireContext().toast("You need to accept the permission in order you can continue")
        }

    }


    private fun eventHandlers(alertDialog: AlertDialog) {

        binding.labelExitApp.setOnClickListener {
            requireActivity().finish()

        }

        binding.labelProceed.setOnClickListener {
            permReqLauncher.launch(appRequestPermission)
        }
    }


}





