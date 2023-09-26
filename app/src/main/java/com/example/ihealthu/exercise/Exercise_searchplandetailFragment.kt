package com.example.ihealthu.exercise

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.ihealthu.EmailStore
import com.example.ihealthu.R
import com.example.ihealthu.databinding.FragmentExerciseMpeditBinding
import com.example.ihealthu.databinding.FragmentExerciseSearchplandetailBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Exercise_searchplandetailFragment : Fragment() {

    val db = Firebase.firestore
    private var _binding: FragmentExerciseSearchplandetailBinding? = null
    private val binding get() = _binding!!
    private var epID: String? = null
    private var epOwner: String? = null
    private var userName: String? = null
    private lateinit var btncancel: Button
    private lateinit var btnimport: Button
    private lateinit var exerciseInput: List<TextView>
    private lateinit var timeInput: List<TextView>
    private val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExerciseSearchplandetailBinding.inflate(inflater, container, false)
        userName = EmailStore.globalEmail.toString()
        btncancel = binding.btnCancel
        btnimport = binding.btnImporteplan

        epID = arguments?.getString("epID")
        epOwner = arguments?.getString("epOwner")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exerciseInput = listOf(
            binding.importe1Content,
            binding.importe2Content,
            binding.importe3Content,
            binding.importe4Content,
            binding.importe5Content,
            binding.importe6Content,
            binding.importe7Content
        )

        timeInput = listOf(
            binding.importt1Content,
            binding.importt2Content,
            binding.importt3Content,
            binding.importt4Content,
            binding.importt5Content,
            binding.importt6Content,
            binding.importt7Content
        )

        epID?.let { nonNullEpID ->
            db.collection("exercise")
                .whereEqualTo("epID", nonNullEpID)
                .whereEqualTo("epOwner", epOwner)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot.documents) {
                        val exerciseData = document.data
                        if (exerciseData != null) {

                            val exerciseName = exerciseData["epID"]?.toString() ?: ""
                            val exerciseDescription = exerciseData["epDesc"]?.toString() ?: ""
                            val exerciseOwner = exerciseData["epOwner"]?.toString() ?: ""

                            binding.inputNewplanname.setText(exerciseName)
                            binding.inputNewplanDescription.setText(exerciseDescription)
                            binding.inputNewplanOwner.setText(exerciseOwner)

                            // Retrieve and populate subcollection data
                            retrieveAndPopulateSubcollection(nonNullEpID)
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("SearchPlanFragment", "Error fetching exercise data: ${e.message}")
                }
        }

        btnimport.setOnClickListener {
            importPlan()
        }

        btncancel.setOnClickListener {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.framelayout_activitymain, Exercise_searchFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    private fun retrieveAndPopulateSubcollection(epID: String) {
        for (day in daysOfWeek) {
            Log.d("SearchPlanFragment", "Retrieving data for day: $day")
            db.collection("exercise")
                .whereEqualTo("epID", epID)
                .whereEqualTo("epOwner", epOwner)
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
                                Log.d("SearchPlanFragment", "Day data for $day: $dayData")
                                if (dayData != null) {
                                    val epContent = dayData["epContent"]?.toString() ?: ""
                                    val eptTime = dayData["eptTime"]?.toString() ?: ""

                                    Log.d("SearchPlanFragment", "Day: $day, epContent: $epContent, eptTime: $eptTime")

                                    // Find the index of the day (e.g., Mon -> 0, Tue -> 1)
                                    val dayIndex = daysOfWeek.indexOf(day)

                                    exerciseInput[dayIndex].setText(epContent)
                                    timeInput[dayIndex].setText(eptTime)
                                } else {
                                    Log.e("SearchPlanFragment", "Day data is null for $day")
                                }
                            }
                            .addOnFailureListener { e ->
                                Log.e("SearchPlanFragment", "Error fetching data for $day: ${e.message}")
                            }
                    } else {
                        Log.e("SearchPlanFragment", "No document found for epID: $epID")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("SearchPlanFragment", "Error fetching document for epID: $epID, ${e.message}")
                }
        }
    }

    private fun importPlan() {

        val data = hashMapOf(
            "epID" to binding.inputNewplanname.text,
            "epDesc" to binding.inputNewplanDescription.text,
            "epOwner" to userName,
            "status" to "no"
        )

        // Add a new document with epID, epDesc, and epOwner
        val newDocumentRef = db.collection("exercise").add(data)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")

                // Now, create a subcollection for daysOfWeek under the newly created document
                val daysCollection = documentReference.collection("DaysOfWeek")

                for (day in daysOfWeek) {
                    val ei = exerciseInput[daysOfWeek.indexOf(day)].text.toString()
                    val et = timeInput[daysOfWeek.indexOf(day)].text.toString()

                    val exerciseData = hashMapOf(
                        "epContent" to ei,
                        "eptTime" to et
                    )

                    // Add the data for the day into the subcollection
                    daysCollection.document(day).set(exerciseData)
                        .addOnSuccessListener {
                            Log.d(TAG, "Data added for $day")
                            val fragmentManager = parentFragmentManager
                            val fragmentTransaction = fragmentManager.beginTransaction()
                            fragmentTransaction.replace(R.id.framelayout_activitymain, Exercise_searchFragment())
                            fragmentTransaction.addToBackStack(null)
                            fragmentTransaction.commit()
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding data for $day", e)
                            Toast.makeText(context, "Failed to add data for $day, try again", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                Toast.makeText(context, "Failed to add document, try again", Toast.LENGTH_SHORT).show()
            }
    }

}