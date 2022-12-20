package com.example.cardapp.fragments.drawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cardapp.databinding.FragmentAddCardBinding
import com.example.cardapp.databinding.FragmentCardsBinding
import com.example.cardapp.databinding.FragmentSignBinding

class AddCardFragment: Fragment() {
    private var _binding: FragmentAddCardBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddCardBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}