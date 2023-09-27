package com.example.ihealthu.diet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.ihealthu.R
import com.example.ihealthu.databinding.FragmentDietSearchDetailBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DietSearchDetailFragment : Fragment() {
    val db = Firebase.firestore
    private var _binding: FragmentDietSearchDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var dsdGetPlan: Button
    private lateinit var dsdBack: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val ownerEmail = arguments?.getString("ownerEmail")
        val planDays = arguments?.getString("planDays")
        val dsdTheday = binding.dsdTheday
        val ppPDesc = binding.ppPDesc
        val bfTime = binding.bfTime
        val bfEsKl = binding.bfEsKl
        val bfRemark = binding.bfRemark
        val luTime = binding.luTime
        val luEsKl = binding.luEsKl
        val luRemark = binding.luRemark
        val dnTime = binding.dnTime
        val dnEsKl = binding.dnEsKl
        val dnRemark = binding.dnRemark

        val dsdGetPlan = binding.dsdGetPlan
        val dsdBack = binding.dsdBack

        return binding.root
    }

}