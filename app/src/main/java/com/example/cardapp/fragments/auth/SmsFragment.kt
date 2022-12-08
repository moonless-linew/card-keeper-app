package com.example.cardapp.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.cardapp.R
import com.example.cardapp.database.DataBase
import com.example.cardapp.databinding.FragmentSmsBinding
import com.example.cardapp.interfaces.OnDownloadCompleteListener
import com.example.cardapp.utils.PhoneUtils
import com.example.cardapp.viewmodels.PhoneSmsFragmentViewModel
import com.fraggjkee.smsconfirmationview.SmsConfirmationView
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.ktx.Firebase
import java.lang.Exception


class SmsFragment : Fragment() {
    var _binding: FragmentSmsBinding? = null
    val binding
        get() = _binding!!
    private val viewmodel: PhoneSmsFragmentViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSmsBinding.inflate(layoutInflater, container, false)
        setupCodeObserver()
        setupSmsView()
        return binding.root
    }

    private fun setupSmsView(){
        binding.smsConfirmationView.startListeningForIncomingMessages()
        binding.smsConfirmationView.onChangeListener =
            SmsConfirmationView.OnChangeListener { code, isComplete ->
                if (code.length == 6){
                    verifyCode(code)
                }
            }
    }
    private fun setupCodeObserver(){
        viewmodel.code.observe(requireActivity()){
            binding.smsConfirmationView.enteredCode = it
        }
    }
    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(
            viewmodel.verificationId.value ?: "0",
            code
        )
        signInWithCredential(credential)
    }
    private fun signInWithCredential(credential: PhoneAuthCredential) {
        Firebase.auth.signInWithCredential(credential).addOnSuccessListener {
            DataBase.downloadUser(it.user?.uid!!, object: OnDownloadCompleteListener{
                override fun onSuccess(document: DocumentSnapshot) {
                    activityNavController().navigateSafely(R.id.action_global_mainFlowFragment)
                }

                override fun onFail(e: Exception) {
                    findNavController().navigateSafely(R.id.action_smsFragment_to_nameFragment)
                }

            })
        }.addOnFailureListener {
            Toast.makeText(requireActivity(), getString(R.string.wrong_code), Toast.LENGTH_SHORT).show()
        }
    }
}