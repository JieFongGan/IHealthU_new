package com.example.ihealthu.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ihealthu.EmailStore
import com.example.ihealthu.R
import com.example.ihealthu.databinding.FragmentUserAddedBMIBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class User_Added_BMI : Fragment() {

    private val db = Firebase.firestore
    private var _binding: FragmentUserAddedBMIBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var Email: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserAddedBMIBinding.inflate(inflater, container, false)
        recyclerView = binding.addedBmiView
        Email = EmailStore.globalEmail.toString()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val bmiAdapter = BmiAdapter(
            requireContext(),
            emptyList<Map<String, Any>>().toMutableList()
        )

        recyclerView.adapter = bmiAdapter

        fetchDataFromFirestore(Email) { data ->
            bmiAdapter.submitList(data)
        }


        val button1: Button = view.findViewById(R.id.btnBack)

        button1.setOnClickListener {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.framelayout_activitymain, User_BMI_Main())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    private fun fetchDataFromFirestore(Email: String, onDataFetched: (List<Map<String, Any>>) -> Unit) {
        val BmiAdapter = mutableListOf<Map<String, Any>>()
        try {
            val collectionName = "bmi"

            db.collection(collectionName)
                .whereEqualTo("email", Email)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot.documents) {
                        val data = document.data
                        if (data != null) {
                            BmiAdapter.add(data)
                        }
                    }
                    onDataFetched(BmiAdapter)
                }
                .addOnFailureListener { e ->
                    Log.e("BMIFragment", "Error fetching data: ${e.message}")
                }
        } catch (e: Exception) {
            Log.e("BMIFragment", "Error fetching data: ${e.message}")
        }
    }

}