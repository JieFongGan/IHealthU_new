package com.example.ihealthu.diet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.ihealthu.R
import com.example.ihealthu.databinding.FragmentDietBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DietFragment : Fragment() {

    private var _binding: FragmentDietBinding? = null
    private val binding get() = _binding!!
    private lateinit var dietTab: TabLayout
    private lateinit var dietTabViewPager: ViewPager2


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDietBinding.inflate(inflater, container, false)
        dietTab = binding.dietTab
        dietTabViewPager = binding.dietTabViewPager

        val adapter = DietPagerAdapter(childFragmentManager, lifecycle)
        dietTabViewPager.adapter = adapter

        // Connect tab and viewpager
        TabLayoutMediator(dietTab, dietTabViewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Current Plan"
                1 -> tab.text = "Search Plan"
                2 -> tab.text = "Create Plan"
            }
        }.attach()

        val tabPosition = arguments?.getInt("viewPagerPosition", 0) ?: 0
        dietTabViewPager.currentItem = tabPosition

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}