package com.example.ihealthu.diet;

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

public class DietPagerAdapter(fm:FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {
    private val fragmentList = listOf(DietOnGoFragment(),DietSearchFragment() ,DietCreateFragment())
    override fun getItemCount(): Int {
        return fragmentList.size
        }

        override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
        }

}
