package com.example.ihealthu.diet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.ihealthu.R
import com.example.ihealthu.databinding.FragmentDietOnGoBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DietOnGoFragment : Fragment() {

    private var _binding: FragmentDietOnGoBinding? = null
    private val binding get() = _binding!!
    private lateinit var dogTab: TabLayout
    private lateinit var dogTabViewPager: ViewPager2
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDietOnGoBinding.inflate(inflater, container, false)
        dogTab = binding.dogTab
        dogTabViewPager = binding.dogTabViewPager

        val adapter = DietOnGoPagerAdapter(childFragmentManager, lifecycle)
        dogTabViewPager.adapter = adapter

        // Connect tab and viewpager
        TabLayoutMediator(dogTab, dogTabViewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Mon"
                1 -> tab.text = "Tue"
                2 -> tab.text = "Wed"
                3 -> tab.text = "Thu"
                4 -> tab.text = "Fri"
                5 -> tab.text = "Sat"
                6 -> tab.text = "Sun"
            }
        }.attach()

        val tabPosition = arguments?.getInt("viewPagerPosition", 0) ?: 0
        dogTabViewPager.currentItem = tabPosition

        return binding.root
    }

}