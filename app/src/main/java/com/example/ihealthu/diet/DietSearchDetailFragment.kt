package com.example.ihealthu.diet

import android.app.AlertDialog
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.ihealthu.EmailStore
import com.example.ihealthu.R
import com.example.ihealthu.databinding.FragmentDietSearchDetailBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DietSearchDetailFragment : Fragment() {
    val db = Firebase.firestore
    private var _binding: FragmentDietSearchDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var etOwnerName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDietSearchDetailBinding.inflate(inflater, container, false)
        etOwnerName = EmailStore.globalEmail.toString()
        //slected OwnerName
        val ownerEmail = arguments?.getString("ownerEmail").toString()
        val planDays = arguments?.getString("planDays").toString()
        Log.d("Debug", "confirm value get in detail $ownerEmail , $planDays")

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

        fetchDataAndPopulateViews(ownerEmail, planDays)

        binding.dsdGetPlan.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Confirm Plan Change")
                .setMessage("Do you really want to change your current plan with this selected plan?")
                .setPositiveButton("Confirm") { _, _ ->
                    updateUserDataWithSelectedPlan(ownerEmail, planDays)
                    Toast.makeText(requireContext(), "Plan changed successfully.", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel") { _, _ ->
                    Toast.makeText(requireContext(), "Plan change cancelled.", Toast.LENGTH_SHORT).show()
                }
                .show()
        }

        binding.dsdBack.setOnClickListener {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.framelayout_activitymain, DietSearchFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        return binding.root
    }
    private fun fetchDataAndPopulateViews(ownerEmail: String, planDays: String) {
        // Fetch data from Firestore based on ownerEmail and planDays
        // For example:
        db.collection("diet")
            .whereEqualTo("dpOwnerName", ownerEmail)
            .whereEqualTo("dpDietDays", planDays)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    binding.dsdOwnerName.text = document.getString("dpOwnerName")
                    binding.dsdTheday.text = document.getString("dpDietDays")
                    binding.ppPDesc.text = document.getString("dpPlanPP")
                    binding.bfTime.text = document.getString("dpBfTime")
                    binding.bfEsKl.text = document.getString("dpBfEsKl")
                    binding.bfRemark.text = document.getString("dpBfRemark")
                    binding.luTime.text = document.getString("dpLuTime")
                    binding.luEsKl.text = document.getString("dpLuEsKl")
                    binding.luRemark.text = document.getString("dpLuRemark")
                    binding.dnTime.text = document.getString("dpDnTime")
                    binding.dnEsKl.text = document.getString("dpDnEsKl")
                    binding.dnRemark.text = document.getString("dpDnRemark")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error adding data *search item")
            }
    }
    private fun updateUserDataWithSelectedPlan(ownerEmail: String, planDays: String) {
        // Fetch data from Firestore based on ownerEmail and planDays
        db.collection("diet")
            .whereEqualTo("dpOwnerName", ownerEmail)
            .whereEqualTo("dpPlanDays", planDays)
            .get()
            .addOnSuccessListener { documents ->
                val dataToUpdate = hashMapOf<String, Any?>()
                for (document in documents) {
                    // Fetch data and populate the HashMap
                    dataToUpdate["dpDietDays"] = document.getString("dpDietDays")
                    dataToUpdate["dpPlanPP"] = document.getString("dpPlanPP")
                    dataToUpdate["dpBftime"] = document.getString("dpBftime")
                    dataToUpdate["dpBfkals"] = document.getString("dpBfkals")
                    dataToUpdate["dpBfRemark"] = document.getString("dpBfRemark")
                    dataToUpdate["dpLutime"] = document.getString("dpLutime")
                    dataToUpdate["dpLukals"] = document.getString("dpLukals")
                    dataToUpdate["dpLuRemark"] = document.getString("dpLuRemark")
                    dataToUpdate["dpDntime"] = document.getString("dpDntime")
                    dataToUpdate["dpDnkals"] = document.getString("dpDnkals")
                    dataToUpdate["dpDnRemark"] = document.getString("dpDnRemark")
                }
                // Now update the Firestore document for the user with etOwnerName
                db.collection("diet")
                    .whereEqualTo("dpOwnerName", etOwnerName)
                    .whereEqualTo("dpPlanDays", planDays)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            db.collection("diet").document(document.id).update(dataToUpdate)
                                .addOnSuccessListener {
                                    Log.d(ContentValues.TAG, "DocumentSnapshot successfully updated!")
                                }
                                .addOnFailureListener { e ->
                                    Log.w(ContentValues.TAG, "Error updating document", e)
                                }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w(ContentValues.TAG, "Error fetching data in import", exception)
                    }
            }
    }//end of update function
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}