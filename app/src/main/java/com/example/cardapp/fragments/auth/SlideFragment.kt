package com.example.cardapp.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.cardapp.databinding.FragmentSlideBinding
import com.example.cardapp.models.Slide
import com.example.cardapp.utils.SlidesUtils

class SlideFragment() : Fragment() {

    private var _binding: FragmentSlideBinding? = null
    private val binding
        get() = _binding!!

    companion object {
        fun newInstance(slide: Slide): SlideFragment {
            val args = Bundle()
            args.putString(SlidesUtils.TITLE, slide.title)
            args.putString(SlidesUtils.DESCRIPTION, slide.description)
            args.putInt(SlidesUtils.IMAGE, slide.image)
            val fragment = SlideFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSlideBinding.inflate(layoutInflater, container, false)
        binding.titleText.text = arguments?.getString(SlidesUtils.TITLE)
        binding.descriptionText.text = arguments?.getString(SlidesUtils.DESCRIPTION)
        binding.imageFeature.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                arguments?.getInt(SlidesUtils.IMAGE) ?: 0,
                null
            )
        )


        return binding.root
    }
}