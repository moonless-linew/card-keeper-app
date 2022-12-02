package com.example.cardapp.fragments.auth
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.cardapp.R
import com.example.cardapp.databinding.FragmentNameBinding
import com.example.cardapp.interfaces.OnAuthCompleteListener
import com.example.cardapp.viewmodels.NameFragmentViewModel


class NameFragment: Fragment(R.layout.fragment_name) {
    private val viewModel: NameFragmentViewModel by viewModels()
    private val binding by viewBinding(FragmentNameBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupContinueButton()
        super.onViewCreated(view, savedInstanceState)
    }


    private fun setupContinueButton(){
        binding.continueButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            binding.continueButton.visibility = View.INVISIBLE
            viewModel.registerUserWithName(binding.editName.text.toString(), object: OnAuthCompleteListener{
                override fun onSuccess() {
                    binding.progressBar.visibility = View.INVISIBLE
                    activityNavController().navigateSafely(R.id.action_global_mainFlowFragment)
                }
                override fun onFail() {

                    binding.progressBar.visibility = View.INVISIBLE
                    binding.continueButton.visibility = View.VISIBLE
                    toastError(getString(R.string.error))
                }

                override fun onAuthFail() {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.continueButton.visibility = View.VISIBLE
                    toastError(getString(R.string.auth_error))
                }
            })
        }


    }
    private fun toastError(msg: String){
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
    }
    private fun Fragment.activityNavController() =
        requireActivity().findNavController(R.id.activityNavigationHost)
    private fun NavController.navigateSafely(@IdRes actionId: Int) {
        currentDestination?.getAction(actionId)?.let { navigate(actionId) }
    }

}