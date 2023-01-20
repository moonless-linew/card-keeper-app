package com.example.cardapp.fragments.drawer


import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.cardapp.R
import com.example.cardapp.adapters.CardsRecyclerAdapter
import com.example.cardapp.databinding.FragmentCardsBinding
import com.example.cardapp.databinding.SheetCardBinding
import com.example.cardapp.datasource.local.location.provideLastLocation
import com.example.cardapp.datasource.local.location.toLocationFlow
import com.example.cardapp.extensions.navigateSafely
import com.example.cardapp.interfaces.CardCallback
import com.example.cardapp.models.Card
import com.example.cardapp.utils.CardsUtils
import com.example.cardapp.viewmodels.CardsFragmentViewModel
import com.example.cardapp.viewmodels.status.CardDataStatus
import com.example.cardapp.viewmodels.status.CompletableTaskStatus
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.zxing.BarcodeFormat


class CardsFragment : Fragment(R.layout.fragment_cards) {
    private val binding by viewBinding(FragmentCardsBinding::bind)
    private val viewModel: CardsFragmentViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    @SuppressLint("MissingPermission")
    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                fusedLocationClient.lastLocation.addOnSuccessListener {
                    viewModel.setCurrentLocation(it)
                    Toast.makeText(requireActivity(), it.latitude.toString() + " " + it.longitude.toString(), Toast.LENGTH_SHORT).show()
                }
                    .addOnFailureListener{
                        Toast.makeText(requireActivity(), "error", Toast.LENGTH_SHORT).show()
                    }

                }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
               toastError(getString(R.string.needPermission))
            }
            else -> {
               toastError(getString(R.string.needPermission))
            }
        }
    }
    private val cardCallback: CardCallback = object : CardCallback {
        override fun onClick(card: Card) {
            BottomSheetDialog(requireActivity()).also { dialog ->
                val dialogBinding: SheetCardBinding = SheetCardBinding.inflate(layoutInflater)
                dialogBinding.bottomSheetCardId.text = card.id
                dialogBinding.bottomSheetMarketName.text = card.market?.name
                dialogBinding.delete.setOnClickListener {
                    viewModel.deleteCard(Firebase.auth.uid!!, card)
                    dialog.dismiss()
                    startLoading()
                }
                dialogBinding.bottomSheetQrView.also { image ->
                    image.setImageBitmap(viewModel.generateQRCodeBitmap(card.id
                        ?: CardsUtils.DEFAULT_ID,
                        BarcodeFormat.valueOf(card.codeType ?: CardsUtils.DEFAULT_FORMAT)))
                }
                dialog.run {
                    this.setContentView(dialogBinding.root)
                    this.show()
                }

            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))

        setupFloatingButton()
        setupObservers()
        setupSwipeRefresh()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupFloatingButton() {
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigateSafely(R.id.action_cardsFragment_to_searchMarketFragment)
        }
    }

    private fun downloadData() {
        viewModel.downloadUserCards(Firebase.auth.uid!!)
        startLoading()
    }

    private fun setupObservers() {
        viewModel.cardsDataStatus.observe(viewLifecycleOwner) {
            when (it) {
                CardDataStatus.Fail -> toastError(getString(R.string.error))
                CardDataStatus.Success -> applyCards()
                CardDataStatus.Empty -> showEmpty()
                CardDataStatus.Null -> downloadData()

            }
        }

        viewModel.deleteCardStatus.observe(viewLifecycleOwner) {
            when (it) {
                CompletableTaskStatus.Fail -> {
                    toastError(getString(R.string.error))
                    stopLoading()
                }
                CompletableTaskStatus.Success -> {
                    downloadData()
                    stopLoading()
                }
            }
        }
    }


    private fun applyCards() {
        binding.cardsRecycler.adapter =
            CardsRecyclerAdapter(viewModel.cardsData, cardCallback)
        stopLoading()
    }

    private fun showEmpty() {
        binding.cardsRecycler.visibility = View.INVISIBLE
        binding.textNothingToShow.visibility = View.VISIBLE
        stopLoading()
    }

    private fun startLoading() {
        binding.cardsRecycler.showShimmerAdapter()
    }

    private fun stopLoading() {
        binding.cardsRecycler.hideShimmerAdapter()
        binding.swipeRefresh.isRefreshing = false
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            downloadData()
        }
    }

    private fun toastError(msg: String) =
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()


}