package com.example.ihealthu.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.ihealthu.EmailStore
import com.example.ihealthu.R
import com.example.ihealthu.databinding.FragmentUserBmiBinding
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date


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

        binding.btnRecord.setOnClickListener {
            val timestamp = Timestamp(Date())

            val emailToSearch = EmailStore.globalEmail
            if(emailToSearch != null) {

                val height = binding.heightPicker.value
                val doubleHeight = height.toDouble() / 100

                val weight = binding.weightPicker.value

                val bmi = weight.toDouble() / (doubleHeight * doubleHeight)

                val bmiData = hashMapOf(
                    "email" to emailToSearch,  // Replace with the user's email
                    "bmi" to bmi,
                    "timestamp" to timestamp
                )
                val db = FirebaseFirestore.getInstance()
                db.collection("bmi")
                    .add(bmiData)
                    .addOnSuccessListener { documentReference ->
                        Toast.makeText(requireContext(), "BMI successful recorded", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(requireContext(), "BMI fail to record", Toast.LENGTH_SHORT).show()
                    }
            }

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