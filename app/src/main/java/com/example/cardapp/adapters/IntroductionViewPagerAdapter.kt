package com.example.cardapp.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cardapp.presentation.fragments.auth.SignFragment
import com.example.cardapp.presentation.fragments.auth.SlideFragment
import com.example.cardapp.models.Slide

class IntroductionViewPagerAdapter(fragment: SignFragment, private val data: List<Slide>):  FragmentStateAdapter(fragment){
    override fun getItemCount(): Int {
        return data.size
    }

    override fun createFragment(position: Int): Fragment {
        return SlideFragment.newInstance(data[position])
    }
    


}
