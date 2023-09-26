package com.example.ihealthu.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.ihealthu.R
import com.example.ihealthu.databinding.FragmentUserBmiBinding


class User_BMI : Fragment() {

    private lateinit var binding: FragmentUserBmiBinding
    private lateinit var resultsTextView: TextView
    private lateinit var healthyTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBmiBinding.inflate(inflater, container, false)

        binding.weightPicker.minValue = 30
        binding.weightPicker.maxValue = 150

        binding.heightPicker.minValue = 100
        binding.heightPicker.maxValue = 250

        resultsTextView = binding.resultsTV
        healthyTextView = binding.healthyTV

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCalculate.setOnClickListener {
            calculateBMI()
        }

        binding.btnBack.setOnClickListener {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.framelayout_activitymain, User_Main())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

    }

    private fun calculateBMI()
    {
        val height = binding.heightPicker.value
        val doubleHeight = height.toDouble() / 100

        val weight = binding.weightPicker.value

        val bmi = weight.toDouble() / (doubleHeight * doubleHeight)

        resultsTextView.text = String.format("Your BMI is: %.2f", bmi)
        healthyTextView.text = String.format("Considered: %s", healthyMessage(bmi))

    }

    private fun healthyMessage(bmi: Double): String
    {
        if (bmi < 18.5)
            return "Underweight"
        if(bmi < 25.0)
            return "Healthy"
        if (bmi < 30.0)
            return "Overweight"

        return "Obese"
    }

}