package com.example.ihealthu.exercise

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.ihealthu.R
import com.example.ihealthu.databinding.FragmentExerciseMpeditBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Exercise_MpeditFragment : Fragment() {

    private val db = Firebase.firestore
    private val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    private var _binding: FragmentExerciseMpeditBinding? = null
    private val binding get() = _binding!!
    private lateinit var btncancel: Button
    private var epID: String? = null
    private lateinit var exerciseInput: List<EditText>
    private lateinit var timeInput: List<EditText>

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

        exerciseInput = listOf(
            binding.edite1Input,
            binding.edite2Input,
            binding.edite3Input,
            binding.edite4Input,
            binding.edite5Input,
            binding.edite6Input,
            binding.edite7Input
        )

        timeInput = listOf(
            binding.editt1Input,
            binding.editt2Input,
            binding.editt3Input,
            binding.editt4Input,
            binding.editt5Input,
            binding.editt6Input,
            binding.editt7Input
        )

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


        val btnUpdate = binding.btnEditeplan
        btnUpdate.setOnClickListener {
            updateDataInFirestore(epID.toString())
        }

    }

    private fun retrieveAndPopulateSubcollection(epID: String) {
        for (day in daysOfWeek) {
            Log.d("MyePlanFragment", "Retrieving data for day: $day")
            db.collection("exercise")
                .whereEqualTo("epID", epID)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        val document = querySnapshot.documents[0]
                        val daysOfWeekCollection = document
                            .reference
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

                                    // Find the index of the day (e.g., Mon -> 0, Tue -> 1)
                                    val dayIndex = daysOfWeek.indexOf(day)

                                    exerciseInput[dayIndex].setText(epContent)
                                    timeInput[dayIndex].setText(eptTime)
                                } else {
                                    Log.e("MyePlanFragment", "Day data is null for $day")
                                }
                            }
                            .addOnFailureListener { e ->
                                Log.e("MyePlanFragment", "Error fetching data for $day: ${e.message}")
                            }
                    } else {
                        Log.e("MyePlanFragment", "No document found for epID: $epID")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("MyePlanFragment", "Error fetching document for epID: $epID, ${e.message}")
                }
        }
    }

    private fun updateDataInFirestore(epID: String) {
        for (day in daysOfWeek) {
            val dayIndex = daysOfWeek.indexOf(day)
            val epContent = exerciseInput[dayIndex].text.toString()
            val eptTime = timeInput[dayIndex].text.toString()

            // Query for documents where the epID field matches your target epID
            db.collection("exercise")
                .whereEqualTo("epID", epID)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot.documents) {
                        val docRef = document.reference
                            .collection("DaysOfWeek")
                            .document(day)

                        val dataToUpdate = mapOf(
                            "epContent" to epContent,
                            "eptTime" to eptTime
                        )

                        docRef.set(dataToUpdate)
                            .addOnSuccessListener {
                                Log.d("MyePlanFragment", "Data for $day updated successfully.")
                            }
                            .addOnFailureListener { e ->
                                Log.e("MyePlanFragment", "Error updating data for $day: ${e.message}")
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("MyePlanFragment", "Error fetching exercise documents: ${e.message}")
                }
        }
    }

}
