package com.example.trazeapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.trazeapp.databinding.FragmentChoicesScreenBinding


class RegisterChoicesFragment : Fragment() {

    private var _binding: FragmentChoicesScreenBinding? = null


    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChoicesScreenBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            cardviewIndividual.setOnClickListener {
                callIndividualFragment()
            }
        }
    }

    private fun callIndividualFragment() {
        val actionToIndividualFragment =
            RegisterChoicesFragmentDirections.actionChoicesFragmentToRegisterFragment()
        findNavController().navigate(actionToIndividualFragment)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}