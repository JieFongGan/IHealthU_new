package com.example.ihealthu.diet

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ihealthu.R
import com.example.ihealthu.databinding.FragmentDietPlanBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DietPlanFragment : Fragment() {

    private var _binding: FragmentDietPlanBinding? = null
    private val binding get() = _binding!!
    private lateinit var dogRecyclerView: RecyclerView
    private lateinit var dogMon: Button
    private lateinit var dogTue: Button
    private lateinit var dogWed: Button
    private lateinit var dogThu: Button
    private lateinit var dogFri: Button
    private lateinit var dogSat: Button
    private lateinit var dogSun: Button
    private lateinit var dogEdit: Button

    val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDietPlanBinding.inflate(inflater, container, false)
        dogRecyclerView = binding.dogRecyclerView

        dogMon = binding.dogMon
        dogTue = binding.dogTue
        dogWed = binding.dogWed
        dogThu = binding.dogThu
        dogFri = binding.dogFri
        dogSat = binding.dogSat
        dogSun = binding.dogSun

        dogEdit = binding.dogEdit
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize RecyclerView with an empty list
        dogRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        dogRecyclerView.adapter = DietPlanAdapter(emptyList<Map<String, Any>>().toMutableList())
        dogRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        //pass value to another frag
        parentFragmentManager.setFragmentResultListener("selectedDay", this) { _, bundle ->
            val selectedDay = bundle.getString("day")}
        // Fetch data for Monday by default
        loadDataForDay("Mon")
        // Set click listeners for each day button
        binding.dogMon.setOnClickListener { loadDataForDay("Mon") }
        binding.dogTue.setOnClickListener { loadDataForDay("Tue") }
        binding.dogWed.setOnClickListener { loadDataForDay("Wed") }
        binding.dogThu.setOnClickListener { loadDataForDay("Thu") }
        binding.dogFri.setOnClickListener { loadDataForDay("Fri") }
        binding.dogSat.setOnClickListener { loadDataForDay("Sat") }
        binding.dogSun.setOnClickListener { loadDataForDay("Sun") }

        // DietPlanAddFragment
        dogEdit.setOnClickListener {
            try {
                val fragmentManager = parentFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.framelayout_activitymain, DietPlanAddFragment())
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            } catch (e: Exception) {
                Log.e("FragmentTransaction", "Error: ${e.message}")
            }
        }
    }
    private fun loadDataForDay(day: String) {
        lifecycleScope.launch {
            val data = fetchDataFromFirestore(day)
            //chg the value
            val bundle = Bundle().apply {
                putString("day", day)
            }
            parentFragmentManager.setFragmentResult("selectedDay", bundle)
            //submitList
            (dogRecyclerView.adapter as DietPlanAdapter).submitList(data)
        }
    }
    private suspend fun fetchDataFromFirestore(day: String): List<Map<String, Any>> {
        return withContext(Dispatchers.IO) {
            val dietDataList = mutableListOf<Map<String, Any>>()
            try {
                val querySnapshot = db.collection("diet")
                    .whereEqualTo("dpDietDays", day)
                    .get()
                    .await()

                for (document in querySnapshot.documents) {
                    Log.d("Firestore", "Document ID: ${document.id} => Document Data: ${document.data}")
                    val data = document.data
                    if (data != null) {
                        dietDataList.add(data)
                    }
                }
            } catch (e: Exception) {
                Log.e("DietPlanFragment", "Error fetching data: ${e.message}")
            }
            return@withContext dietDataList
        }
    }

//    fun loadDataForDay(day: String) {
//
//        val dietPlanList = mutableListOf<Map<String, Any>>()
//        db.collection("diet")
//            .whereEqualTo("dpDietDays", day)
//            .get()
//            .addOnSuccessListener { documents ->
//                for (document in documents) {
//                    val dietPlanMap = document.data  // This will be a Map<String, Any>
//                    dietPlanList.add(dietPlanMap)
//                }
//                // Update the RecyclerView here
//                // You'll need to modify your RecyclerView Adapter to work with Map<String, Any>
//            }
//            .addOnFailureListener { exception ->
//                Log.e("Firebase", "Error fetching data: ", exception)
//            }
//    }

}
//        val sampleDietPlans = listOf(
//            DietPlan(dpID = "101", dpPlanName = "testPlan", dpOwnerName = "testName", dpAgeRange = "32", dpPurpose = "GG.com",
//                dpDays = "Mon",
//                dpBftime = "9.30am", dpBfRatio = "p3s4b3", dpBfEsKl = "300kal", dpBfRemark = "eat",
//                dpLutime = "9.30am", dpLuRatio = "p3s4b3", dpLuEsKl = "300kal", dpLuRemark = "eat",
//                dpDntime = "9.30am", dpDnRatio = "p3s4b3", dpDnEsKl = "300kal", dpDnRemark = "eat"),
//            DietPlan(dpID = "102", dpPlanName = "testPlan", dpOwnerName = "testName", dpAgeRange = "32", dpPurpose = "GG.com",
//                dpDays = "Tue",
//                dpBftime = "9.00am", dpBfRatio = "p3s4b3", dpBfEsKl = "300kal", dpBfRemark = "eat",
//                dpLutime = "9.00am", dpLuRatio = "p3s4b3", dpLuEsKl = "300kal", dpLuRemark = "eat",
//                dpDntime = "9.00am", dpDnRatio = "p3s4b3", dpDnEsKl = "300kal", dpDnRemark = "eat")
//        )
//        // Initialize with Monday's data
//        val realData = database.dietPlanDao.getPlansByDay("Mon")
//        if (realData.isEmpty()) {
//            database.dietPlanDao.insert(sampleDietPlans[0])
//        } else {
//            adapter.submitList(realData)
//        }
//private fun setupRecyclerView() {
//    dogRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//    adapter = DogRecyclerViewAdapter()
//    dogRecyclerView.adapter = adapter
//}
//
//private fun loadDataForDay(day: String) {
//    lifecycleScope.launch {
//        val plans: List<DietPlan> = withContext(Dispatchers.IO) {
//            val data = database.dietPlanDao.getPlansByDay(day)
//            Log.d("DatabaseDebug", "Data for $day: $data")
//            data
//        }
//        adapter.submitList(plans)
//    }
//}