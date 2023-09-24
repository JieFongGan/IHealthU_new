package com.example.ihealthu.exercise

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.ihealthu.R
import com.example.ihealthu.databinding.FragmentExerciseMpeditBinding
import com.example.ihealthu.databinding.FragmentExerciseMyplanBinding
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Exercise_MpeditFragment : Fragment() {

    private val db = Firebase.firestore
    private val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    private var _binding: FragmentExerciseMpeditBinding? = null
    private val binding get() = _binding!!
    private lateinit var btncancel: Button
    private var epID: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExerciseMpeditBinding.inflate(inflater, container, false)
        btncancel = binding.btnCancel
        epID = arguments?.getString("epID")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        epID?.let { nonNullEpID ->
            db.collection("exercise")
                .whereEqualTo("epID", nonNullEpID)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot.documents) {
                        val exerciseData = document.data
                        if (exerciseData != null) {

                            val exerciseName = exerciseData["epID"]?.toString() ?: ""
                            val exerciseDescription = exerciseData["epDesc"]?.toString() ?: ""

                            binding.inputNewplanname.setText(exerciseName)
                            binding.inputNewplanDescription.setText(exerciseDescription)

                            // Retrieve and populate subcollection data
                            retrieveAndPopulateSubcollection(nonNullEpID)
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("MyePlanFragment", "Error fetching exercise data: ${e.message}")
                }
        }
    }

    private fun retrieveAndPopulateSubcollection(epID: String) {
        for (day in daysOfWeek) {
            Log.d("MyePlanFragment", "Retrieving data for day: $day")
            val daysOfWeekCollection = db.collection("exercise")
                .document(epID)
                .collection("DaysOfWeek")
                .document(day)

            daysOfWeekCollection
                .get()
                .addOnSuccessListener { dayDocumentSnapshot ->
                    val dayData = dayDocumentSnapshot.data
                    Log.d("MyePlanFragment", "Day data for $day: $dayData")
                    if (dayData != null) {
                        val epContent = dayData["epContent"]?.toString() ?: ""
                        val eptTime = dayData["eptTime"]?.toString() ?: ""

                        Log.d("MyePlanFragment", "Day: $day, epContent: $epContent, eptTime: $eptTime")

                        val exerciseInput = binding::class.java.getField("dailye${daysOfWeek.indexOf(day) + 1}_input")
                        val timeInput = binding::class.java.getField("dailyt${daysOfWeek.indexOf(day) + 1}_input")
                        exerciseInput.set(binding, epContent)
                        timeInput.set(binding, eptTime)
                    } else {
                        Log.e("MyePlanFragment", "Day data is null for $day")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("MyePlanFragment", "Error fetching data for $day: ${e.message}")
                }
        }
    }

}

