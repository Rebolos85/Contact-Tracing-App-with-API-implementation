package com.example.trazeapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.example.trazeapp.databinding.QrCodeFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QrCodeFragment : Fragment() {
    private var _binding: QrCodeFragmentBinding? = null


    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = QrCodeFragmentBinding.inflate(inflater, container, false)
        return binding.root


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}