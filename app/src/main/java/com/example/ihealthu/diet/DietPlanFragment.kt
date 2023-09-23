//package com.example.ihealthu.diet
//
//import android.os.Bundle
//import android.util.Log
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import androidx.lifecycle.lifecycleScope
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.ihealthu.database.DietPlan
//import com.example.ihealthu.database.IHUdatabase
//import com.example.ihealthu.databinding.FragmentDietPlanBinding
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//class DietPlanFragment : Fragment() {
//
//    private var _binding: FragmentDietPlanBinding? = null
//    private val binding get() = _binding!!
//    private lateinit var dogRecyclerView: RecyclerView
//    private lateinit var dogMon: Button
//    private lateinit var dogTue: Button
//    private lateinit var dogWed: Button
//    private lateinit var dogThu: Button
//    private lateinit var dogFri: Button
//    private lateinit var dogSat: Button
//    private lateinit var dogSun: Button
//    private lateinit var dogEdit: Button
//
//    private lateinit var adapter: DogRecyclerViewAdapter
//    private lateinit var database: IHUdatabase
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        _binding = FragmentDietPlanBinding.inflate(inflater, container, false)
//        dogRecyclerView = binding.dogRecyclerView
//
//        dogMon = binding.dogMon
//        dogTue = binding.dogTue
//        dogWed = binding.dogWed
//        dogThu = binding.dogThu
//        dogFri = binding.dogFri
//        dogSat = binding.dogSat
//        dogSun = binding.dogSun
//        dogEdit = binding.dogEdit
//        return binding.root
//    }
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        setupRecyclerView()
//        database = IHUdatabase.getInstance(requireContext())
//
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
//        // Initialize with Monday's data
////        loadDataForDay("Mon")
//        // Set up click listeners for each button to load data for that day
//        dogMon.setOnClickListener { loadDataForDay("Mon") }
//        dogTue.setOnClickListener { loadDataForDay("Tue") }
//        dogWed.setOnClickListener { loadDataForDay("Wed") }
//        dogThu.setOnClickListener { loadDataForDay("Thu") }
//        dogFri.setOnClickListener { loadDataForDay("Fri") }
//        dogSat.setOnClickListener { loadDataForDay("Sat") }
//        dogSun.setOnClickListener { loadDataForDay("Sun") }
//
//        dogEdit.setOnClickListener {
//            // Implement edit functionality here
//        }
//    }
//
//    private fun setupRecyclerView() {
//        dogRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//        adapter = DogRecyclerViewAdapter()
//        dogRecyclerView.adapter = adapter
//    }
//
//    private fun loadDataForDay(day: String) {
//        lifecycleScope.launch {
//            val plans: List<DietPlan> = withContext(Dispatchers.IO) {
//                val data = database.dietPlanDao.getPlansByDay(day)
//                Log.d("DatabaseDebug", "Data for $day: $data")
//                data
//            }
//            adapter.submitList(plans)
//        }
//    }
//
//}