package com.example.cardapp.presentation.fragments.drawer


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.cardapp.R
import com.example.cardapp.presentation.adapters.CardsRecyclerAdapter
import com.example.cardapp.databinding.FragmentCardsBinding
import com.example.cardapp.databinding.SheetCardBinding
import com.example.cardapp.extensions.navigateSafely
import com.example.cardapp.presentation.adapters.CardCallback
import com.example.cardapp.domain.model.Card
import com.example.cardapp.utils.CardsUtils
import com.example.cardapp.presentation.viewmodels.CardsFragmentViewModel
import com.example.cardapp.presentation.model.status.CardDataStatus
import com.example.cardapp.presentation.model.status.CompletableTaskStatus
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.zxing.BarcodeFormat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardsFragment : Fragment(R.layout.fragment_cards) {
    private val binding by viewBinding(FragmentCardsBinding::bind)
    private val viewModel: CardsFragmentViewModel by viewModels()
    private val cardCallback: CardCallback = object : CardCallback {
        override fun onClick(card: Card) {
            provideQRDialog(card)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupFloatingButton()
        setupObservers()
        setupSwipeRefresh()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupFloatingButton() {
        binding.floatingActionButton.setOnClickListener {
            val cardsStatus = viewModel.cardsDataStatus.value
            val marketsIds = if (cardsStatus is CardDataStatus.Success)
                cardsStatus.cards.mapNotNull { it.marketID }.toTypedArray()
            else emptyArray()
            val action = CardsFragmentDirections.actionCardsFragmentToSearchMarketFragment(marketsIds)
            findNavController().navigate(action)
        }
    }

    private fun checkPermission(): Boolean = ActivityCompat.checkSelfPermission(
        requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        requireActivity(),
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED

    private fun getCardsData() {
        val uid = Firebase.auth.uid ?: return
        val locationClient = if (checkPermission())
            LocationServices.getFusedLocationProviderClient(requireActivity())
        else null
        viewModel.getUsersCards(uid, locationClient)
        startLoading()
    }

    private fun setupObservers() {
        viewModel.cardsDataStatus.observe(viewLifecycleOwner) {
            when (it) {
                CardDataStatus.Fail -> showEmpty(isError = true)
                is CardDataStatus.Success -> applyCards(it.cards)
                CardDataStatus.Empty -> showEmpty()
                CardDataStatus.Null -> getCardsData()
            }
        }

        viewModel.deleteCardStatus.observe(viewLifecycleOwner) {
            when (it) {
                CompletableTaskStatus.Fail -> {
                    toastError(getString(R.string.error))
                    stopLoading()
                }
                CompletableTaskStatus.Success -> {
                    getCardsData()
                    stopLoading()
                }
            }
        }
    }

    private fun applyCards(cards: List<Card>) {
        binding.cardsRecycler.adapter =
            CardsRecyclerAdapter(cards, cardCallback)
        stopLoading()
    }

    private fun showEmpty(isError: Boolean = false) {
        binding.cardsRecycler.visibility = View.INVISIBLE
        binding.textNothingToShow.visibility = View.VISIBLE
        val textLabel = if (isError) R.string.error else R.string.nothing_to_show
        binding.textNothingToShow.text = requireContext().getString(textLabel)
        stopLoading()
    }

    private fun startLoading() {
        binding.textNothingToShow.visibility = View.GONE
        binding.cardsRecycler.showShimmerAdapter()
    }

    private fun stopLoading() {
        binding.cardsRecycler.hideShimmerAdapter()
        binding.swipeRefresh.isRefreshing = false
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            getCardsData()
        }
    }

    private fun provideQRDialog(card: Card) {
        val dialog = BottomSheetDialog(requireActivity())
        val dialogBinding: SheetCardBinding = SheetCardBinding.inflate(layoutInflater).apply {
            bottomSheetCardId.text = card.id
            bottomSheetMarketName.text = card.marketNetwork?.name
            delete.setOnClickListener {
                viewModel.deleteCard(Firebase.auth.uid!!, card)
                dialog.dismiss()
                startLoading()
            }
            bottomSheetQrView.also { image ->
                image.setImageBitmap(
                    viewModel.generateQRCodeBitmap(
                        card.id ?: CardsUtils.DEFAULT_ID,
                        BarcodeFormat.valueOf(card.codeType ?: CardsUtils.DEFAULT_FORMAT)
                    )
                )
            }
        }
        dialog.apply {
            setContentView(dialogBinding.root)
            show()
        }
    }

    private fun toastError(msg: String) =
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
}
