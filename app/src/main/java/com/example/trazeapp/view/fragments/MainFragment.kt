package com.example.trazeapp.view.fragments;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.trazeapp.databinding.FragmentMainBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainFragment : Fragment() {


    private var _binding: FragmentMainBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogout.setOnClickListener {
            Firebase.auth.signOut()
            backToFirstScreenFragment()

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun backToFirstScreenFragment() {
        val action = MainFragmentDirections.actionMainFragmentToMainScreenFragment()
        findNavController().navigate(action)
    }


}
