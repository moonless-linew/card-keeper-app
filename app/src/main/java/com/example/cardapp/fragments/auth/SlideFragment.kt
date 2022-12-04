package com.example.cardapp.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.cardapp.databinding.FragmentSlideBinding
import com.example.cardapp.models.Slide
import com.example.cardapp.utils.SlideKeys

class SlideFragment() : Fragment() {

    private var _binding: FragmentSlideBinding? = null
    private val binding
        get() = _binding!!

    companion object {
        fun newInstance(slide: Slide): SlideFragment {
            val args = Bundle()
            args.putString(SlideKeys.TITLE, slide.title)
            args.putString(SlideKeys.DESCRIPTION, slide.description)
            args.putInt(SlideKeys.IMAGE, slide.image)
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
        binding.titleText.text = arguments?.getString(SlideKeys.TITLE)
        binding.descriptionText.text = arguments?.getString(SlideKeys.DESCRIPTION)
        binding.imageFeature.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                arguments?.getInt(SlideKeys.IMAGE) ?: 0,
                null
            )
        )


        return binding.root
    }
}