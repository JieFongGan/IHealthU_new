package com.example.ihealthu.diet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.ihealthu.R
import com.example.ihealthu.databinding.FragmentDietOnGoBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DietOnGoFragment : Fragment() {

    private var _binding: FragmentDietOnGoBinding? = null
    private val binding get() = _binding!!
    private lateinit var dogRecyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDietOnGoBinding.inflate(inflater, container, false)
        dogRecyclerView = binding.dogRecyclerView
        return binding.root
    }


}