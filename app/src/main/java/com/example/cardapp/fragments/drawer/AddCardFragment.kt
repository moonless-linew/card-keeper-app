package com.example.cardapp.fragments.drawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cardapp.R
import com.example.cardapp.databinding.FragmentAddCardBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class AddCardFragment: Fragment() {
    private var _binding: FragmentAddCardBinding? = null
    private val binding
        get() = _binding!!
    private val barcodeScanLauncher = registerForActivityResult(ScanContract()) {
        if (it.contents == null) {
            Toast.makeText(requireActivity(), "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireActivity(), "Scanned: " + it.formatName, Toast.LENGTH_LONG)
                .show()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCardBinding.inflate(layoutInflater, container, false)
        setupScanButton()

        return binding.root
    }
    private fun setupScanButton(){
        binding.scanCodeButton.setOnClickListener {
            barcodeScanLauncher.launch(ScanOptions().also {
                it.setOrientationLocked(true)
                it.setBarcodeImageEnabled(true)
                it.setPrompt(getString(R.string.scan_card))

            })
        }
    }
}