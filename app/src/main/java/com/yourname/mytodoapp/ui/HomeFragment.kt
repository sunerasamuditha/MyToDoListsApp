package com.yourname.mytodoapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.yourname.mytodoapp.R
import com.yourname.mytodoapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // **IMPORTANT**: Replace with your actual name and ID
        binding.welcomeMessage.text = "Welcome to Sunera's TO DO List!"
        binding.developerInfo.text = "This App was developed by G K S Samuditha                                " +
                "    (D/BCE/24/0001)."

        binding.goToListsButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_allListsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}