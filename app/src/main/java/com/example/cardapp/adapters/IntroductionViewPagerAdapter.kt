package com.example.cardapp.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cardapp.fragments.SlideFragment
import com.example.cardapp.fragments.auth.SignFragment

class IntroductionViewPagerAdapter(fragment: SignFragment):  FragmentStateAdapter(fragment){
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return SlideFragment.newInstance(position)
    }

}