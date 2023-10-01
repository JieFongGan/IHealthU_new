package com.example.ihealthu.diet

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
import com.example.ihealthu.R
import com.example.ihealthu.databinding.FragmentDietPlanBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.Serializable
import kotlinx.coroutines.*
import android.app.AlertDialog
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.ihealthu.EmailStore
import com.example.ihealthu.diet.DietPlanViewModel

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
    private lateinit var dogDelete: Button
    private lateinit var theday: String

    private lateinit var dogSearch: Button

    //user name
    private lateinit var etOwnerName: String

    private var documentId: String?=null

    val db = Firebase.firestore

    private val viewModel: DietPlanViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDietPlanBinding.inflate(inflater, container, false)
        dogRecyclerView = binding.dogRecyclerView
        //user name
        etOwnerName = EmailStore.globalEmail.toString()
        theday = "Mon"

        dogMon = binding.dogMon
        dogTue = binding.dogTue
        dogWed = binding.dogWed
        dogThu = binding.dogThu
        dogFri = binding.dogFri
        dogSat = binding.dogSat
        dogSun = binding.dogSun

        dogEdit = binding.dogEdit
        dogDelete = binding.dogDelete

        dogSearch = binding.dogSearch
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize RecyclerView with an empty list
        dogRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        dogRecyclerView.adapter = DietPlanAdapter(emptyList<Map<String, Any>>().toMutableList())
        dogRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        // Observe changes in data and update UI
        viewModel.dietData.observe(viewLifecycleOwner) { data ->
            (dogRecyclerView.adapter as DietPlanAdapter).submitList(data)
        }
        // Observe changes in selected day
        viewModel.daySelected.observe(viewLifecycleOwner) { day ->
            theday = day
        }

        // Fetch data for Monday by default
        viewModel.fetchDataFromFirestore("Mon", etOwnerName)
        // Set click listeners for each day button
        binding.dogMon.setOnClickListener { loadDataForDay("Mon",etOwnerName) }
        binding.dogTue.setOnClickListener { loadDataForDay("Tue",etOwnerName) }
        binding.dogWed.setOnClickListener { loadDataForDay("Wed",etOwnerName) }
        binding.dogThu.setOnClickListener { loadDataForDay("Thu",etOwnerName) }
        binding.dogFri.setOnClickListener { loadDataForDay("Fri",etOwnerName) }
        binding.dogSat.setOnClickListener { loadDataForDay("Sat",etOwnerName) }
        binding.dogSun.setOnClickListener { loadDataForDay("Sun",etOwnerName) }

        // Edit button DietPlanAddFragment
        dogEdit.setOnClickListener {
            try {
                // Fetch data for the day
                viewModel.dietData.observe(viewLifecycleOwner) { dataForTheDay ->
                    val documentIdp = documentId
                    Log.d("Firestore", "Document ID set to inedit button: $documentIdp")
                    setFragmentResult(
                        "dietPlanData",
                        bundleOf("dayData" to dataForTheDay as Serializable)
                    )
                    setFragmentResult("documentId", bundleOf("docID" to documentIdp))

                    // navi to PlanAdd
                    val fragmentManager = parentFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(
                        R.id.framelayout_activitymain,
                        DietPlanAddFragment()
                    )
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()

                    // Remove observer to prevent repeated calls
                    viewModel.dietData.removeObservers(viewLifecycleOwner)
                }
            } catch (e: Exception) {
                Log.e("FragmentTransaction", "Error: ${e.message}")
            }
        }
        //DietPlanDelete
        dogDelete.setOnClickListener {
            // Create an AlertDialog Builder
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Delete Confirmation")
            builder.setMessage("Are you sure you want to delete this data?")
            // Add "Yes" button
            builder.setPositiveButton("Yes") { dialog, which ->
                try {
                    lifecycleScope.launch {
                        deleteDataFromFirestore(theday, etOwnerName)
                    }
                    Toast.makeText(context,"$theday data deleted",Toast.LENGTH_SHORT).show()
                    val fragmentManager = parentFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.framelayout_activitymain, DietPlanFragment())
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                } catch (e: Exception) {
                    Log.e("DietPlanFragment", "Error deleting data: ${e.message}")
                }
            }
            //No button
            builder.setNegativeButton("No") { dialog, which ->
                // Do nothing
            }
            val dialog = builder.create()
            dialog.show()
        }
        //DietPlanSearch
        dogSearch.setOnClickListener{
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.framelayout_activitymain, DietSearchFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }//end of OnCreate View
    //weekly button fetch data
    private fun loadDataForDay(day: String, ownerName: String) {
        lifecycleScope.launch {
            viewModel.fetchDataFromFirestore(day, ownerName)
            //chg the value
            theday = day
            setFragmentResult("selectedDay", bundleOf("day" to day))
        }
    }

    //delete method
    private suspend fun deleteDataFromFirestore(day: String, ownerName: String) {
        withContext(Dispatchers.IO) {
            try {
                val querySnapshot = db.collection("diet")
                    .whereEqualTo("dpDietDays", day)
                    .whereEqualTo("dpOwnerName", ownerName)
                    .get()
                    .await()

                for (document in querySnapshot.documents) {
                    db.collection("diet").document(document.id).delete().await()
                    Log.d("DietPlanFragment", "Document with ID: ${document.id} deleted.")
                }
            } catch (e: Exception) {
                Log.e("DietPlanFragment", "Error deleting data: ${e.message}")
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}