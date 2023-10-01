package com.example.ihealthu.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ihealthu.EmailStore
import com.example.ihealthu.R
import com.example.ihealthu.databinding.FragmentHomeBinding
import com.example.ihealthu.profile.User_BMI
import com.example.ihealthu.profile.User_Personal_Profile
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Calendar

class HomeFragment : Fragment() {

    val db = Firebase.firestore
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var HomeExerciseView: RecyclerView
    private lateinit var OwnerName: String
    private lateinit var homeAdapter: HomeAdapter
    private var selectedDay: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        OwnerName = EmailStore.globalEmail.toString()

        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        selectedDay = when (dayOfWeek) {
            Calendar.SUNDAY -> "Sun"
            Calendar.MONDAY -> "Mon"
            Calendar.TUESDAY -> "Tue"
            Calendar.WEDNESDAY -> "Wed"
            Calendar.THURSDAY -> "Thu"
            Calendar.FRIDAY -> "Fri"
            Calendar.SATURDAY -> "Sat"
            else -> ""
        }

        HomeExerciseView = binding.homeegView
        val layoutManager = LinearLayoutManager(requireContext())
        HomeExerciseView.layoutManager = layoutManager

        homeAdapter = HomeAdapter(emptyList<Map<String, Any>>().toMutableList(), selectedDay)
        HomeExerciseView.adapter = homeAdapter

        loadDataForDay(selectedDay, OwnerName)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = FirebaseFirestore.getInstance()

        val emailToSearch = EmailStore.globalEmail
        if(emailToSearch != null){

            db.collection("user")
                .whereEqualTo("email", emailToSearch)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val document = documents.documents[0] // Assuming there's only one user with the specified email

                        // Retrieve user data
                        val profilePicUrl = document["profilepic"] as? String

                        // Load and display profile picture if available
                        if (!profilePicUrl.isNullOrEmpty()) {
                            loadProfilePicture(profilePicUrl)
                        }

                    } else {
                        Toast.makeText(requireContext(), "No user found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "failed, try again", Toast.LENGTH_SHORT).show()
                }
        }

        binding.userProfileIcon.setOnClickListener {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.framelayout_activitymain, User_Personal_Profile())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        binding.btnGoBMI.setOnClickListener {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.framelayout_activitymain, User_BMI())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

    }

    private fun loadProfilePicture(profilePicUrl: String) {
        Glide.with(requireContext())
            .load(profilePicUrl)
            .into(binding.userProfileIcon)
    }

    private fun loadDataForDay(day: String, ownerName: String) {
        lifecycleScope.launch {
            val data = fetchDataFromFirestore(day, ownerName)
            homeAdapter.submitList(data)
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
                    val data = document.data

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
                Log.e("HomeFragment", "Error fetching data: ${e.message}")
            }

            if (exerciseDataList.isEmpty()) {
                exerciseDataList.add(mapOf("no_plans" to true))
            }

            return@withContext exerciseDataList
        }
    }
}
