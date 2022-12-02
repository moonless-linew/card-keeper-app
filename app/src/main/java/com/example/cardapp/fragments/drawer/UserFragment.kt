package com.example.cardapp.fragments.drawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.cardapp.R
import com.example.cardapp.databinding.FragmentUserBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UserFragment: Fragment() {
    private var _binding: FragmentUserBinding? = null
    private val binding
        get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(layoutInflater, container, false)
        binding.logOutButton.setOnClickListener{
            Firebase.auth.signOut()
            activityNavController().navigate(R.id.action_global_signFlowFragment)
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    private fun Fragment.activityNavController() = requireActivity().findNavController(R.id.activityNavigationHost)


}