package com.example.cardapp.presentation.fragments.drawer


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.cardapp.R
import com.example.cardapp.adapters.CardsRecyclerAdapter
import com.example.cardapp.databinding.FragmentCardsBinding
import com.example.cardapp.databinding.SheetCardBinding
import com.example.cardapp.extensions.navigateSafely
import com.example.cardapp.interfaces.CardCallback
import com.example.cardapp.models.Card
import com.example.cardapp.utils.CardsUtils
import com.example.cardapp.presentation.viewmodels.CardsFragmentViewModel
import com.example.cardapp.presentation.model.status.CardDataStatus
import com.example.cardapp.presentation.model.status.CompletableTaskStatus
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.zxing.BarcodeFormat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardsFragment : Fragment(R.layout.fragment_cards) {
    private val binding by viewBinding(FragmentCardsBinding::bind)
    private val viewModel: CardsFragmentViewModel by viewModels()
    private val cardCallback: CardCallback =  object: CardCallback{
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
                    image.setImageBitmap(viewModel.generateQRCodeBitmap(card.id ?: CardsUtils.DEFAULT_ID,
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
            when(it){
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
