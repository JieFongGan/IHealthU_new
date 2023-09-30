package com.example.ihealthu.exercise

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ihealthu.EmailStore
import com.example.ihealthu.EmailStore.globalEmail
import com.example.ihealthu.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ExerciseFragment : Fragment() {

    val db = Firebase.firestore

    private lateinit var dailyRecyclerView: RecyclerView
    private lateinit var btntomyeplan: Button
    private lateinit var btntosearcheplan: Button
    private var selectedDay: String = "Mon"
    private lateinit var exerciseAdapter: ExercisePlanAdapter
    private lateinit var OwnerName: String

    //7day button
    private lateinit var btnmonday: Button
    private lateinit var btntuesday: Button
    private lateinit var btnwednesday: Button
    private lateinit var btnthursday: Button
    private lateinit var btnfriday: Button
    private lateinit var btnsaturday: Button
    private lateinit var btnsunday: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_exercise, container, false)

        dailyRecyclerView = view.findViewById(R.id.dailyeg_View)
        btntomyeplan = view.findViewById(R.id.btn_myeplan)
        btntosearcheplan = view.findViewById(R.id.btn_searcheplan)
        OwnerName = EmailStore.globalEmail.toString()

        loadDataForDay(selectedDay, OwnerName)

        //7day button
        btnmonday = view.findViewById(R.id.btn_monday )
        btntuesday = view.findViewById(R.id.btn_tuesday)
        btnwednesday = view.findViewById(R.id.btn_wednesday)
        btnthursday = view.findViewById(R.id.btn_thursday)
        btnfriday = view.findViewById(R.id.btn_friday )
        btnsaturday = view.findViewById(R.id.btn_saturday)
        btnsunday = view.findViewById(R.id.btn_sunday)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dailyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        exerciseAdapter = ExercisePlanAdapter(emptyList<Map<String, Any>>().toMutableList(), selectedDay)
        dailyRecyclerView.adapter = exerciseAdapter

        btnmonday.setOnClickListener {
            selectedDay = "Mon"
            loadDataForDay(selectedDay, OwnerName)
        }

        btntuesday.setOnClickListener {
            selectedDay = "Tue"
            loadDataForDay(selectedDay, OwnerName)
        }

        btnwednesday.setOnClickListener {
            selectedDay = "Wed"
            loadDataForDay(selectedDay, OwnerName)
        }

        btnthursday.setOnClickListener {
            selectedDay = "Thu"
            loadDataForDay(selectedDay, OwnerName)
        }

        btnfriday.setOnClickListener {
            selectedDay = "Fri"
            loadDataForDay(selectedDay, OwnerName)
        }

        btnsaturday.setOnClickListener {
            selectedDay = "Sat"
            loadDataForDay(selectedDay, OwnerName)
        }

        btnsunday.setOnClickListener {
            selectedDay = "Sun"
            loadDataForDay(selectedDay, OwnerName)
        }

        btntomyeplan.setOnClickListener {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.framelayout_activitymain, Exercise_MyplanFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        btntosearcheplan.setOnClickListener {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.framelayout_activitymain, Exercise_searchFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    private fun loadDataForDay(day: String, ownerName: String) {
        lifecycleScope.launch {
            val data = fetchDataFromFirestore(day, ownerName)
            setFragmentResult("selectedDay", bundleOf("day" to day))
            exerciseAdapter.submitList(data)
        }
    }

    private suspend fun fetchDataFromFirestore(day: String, ownerName: String): List<Map<String, Any>> {
        return withContext(Dispatchers.IO) {
            val exerciseDataList = mutableListOf<Map<String, Any>>()
            try {
                val querySnapshot = db.collection("exercise")
                    .whereEqualTo("epOwner", ownerName)
                    .whereEqualTo("status", "Yes")
                    .get()
                    .await()

                for (document in querySnapshot.documents) {
                    // Fetch data from the main document
                    val data = document.data

                    // Retrieve data for the selected day from the subcollection
                    val subdocument = document.reference
                        .collection("DaysOfWeek")
                        .document(day)
                        .get()
                        .await()

                    val subdocumentData = subdocument.data
                    if (subdocumentData != null) {
                        data?.putAll(subdocumentData)
                    }

                    if (data != null) {
                        exerciseDataList.add(data)
                    }
                }
            } catch (e: Exception) {
                Log.e("ExerciseFragment", "Error fetching data: ${e.message}")
            }

            // If exerciseDataList is empty, return a placeholder value indicating no plans
            if (exerciseDataList.isEmpty()) {
                exerciseDataList.add(mapOf("no_plans" to true))
            }

            return@withContext exerciseDataList
        }
    }
}
