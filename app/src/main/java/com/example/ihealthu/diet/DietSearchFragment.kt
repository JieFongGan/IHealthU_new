package com.example.ihealthu.diet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.ihealthu.R
import androidx.appcompat.widget.SearchView
import com.example.ihealthu.databinding.FragmentDietSearchBinding
import android.widget.TextView
import com.google.firebase.firestore.QuerySnapshot

class DietSearchFragment : Fragment() {
    private var _binding: FragmentDietSearchBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private lateinit var searchPlanView: SearchView
    private lateinit var dsRecyclerView: RecyclerView
    private lateinit var dsBack: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDietSearchBinding.inflate(inflater, container, false)
        searchPlanView = binding.searchPlanView
        dsRecyclerView = binding.dsRecyclerView
        dsBack = binding.dsBack

        //adapter init
        val dietSearchAdapter = DietSearchAdapter(mutableListOf())
        dsRecyclerView.layoutManager = LinearLayoutManager(context)
        dsRecyclerView.adapter = dietSearchAdapter
        //getdata form firestore
        var searchResults = mutableListOf<Map<String, Any>>()
        // Fetch data from Firestore
        db.collection("diet")
            .addSnapshotListener { querySnapshot: QuerySnapshot?, _ ->
                if (querySnapshot != null) {
                    val searchResults = querySnapshot.documents.map { it.data }
                    dietSearchAdapter.dietDataList.clear()
                    dietSearchAdapter.dietDataList.addAll(searchResults as MutableList<Map<String, Any>>)
                    dietSearchAdapter.notifyDataSetChanged()
                }
            }

        return binding.root
    }

}
