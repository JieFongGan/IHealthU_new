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
import android.widget.Toast
import com.google.firebase.firestore.QuerySnapshot

class DietSearchFragment : Fragment() {
    private var _binding: FragmentDietSearchBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private lateinit var searchPlanView: SearchView
    private lateinit var dsRecyclerView: RecyclerView
    private lateinit var dsBack: Button
    private lateinit var dsSearchWeb: Button
    private var searchResults = mutableListOf<Map<String, Any>>()
    private lateinit var dietSearchAdapter: DietSearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDietSearchBinding.inflate(inflater, container, false)
        searchPlanView = binding.searchPlanView
        dsRecyclerView = binding.dsRecyclerView
        dsBack = binding.dsBack
        dsSearchWeb = binding.dsSearchWeb
        //adapter init
        dietSearchAdapter = DietSearchAdapter(mutableListOf(), parentFragmentManager)
        dsRecyclerView.layoutManager = LinearLayoutManager(context)
        dsRecyclerView.adapter = dietSearchAdapter
        //getdata form firestore
        db.collection("diet")
            .addSnapshotListener { querySnapshot: QuerySnapshot?, _ ->
                if (querySnapshot != null) {
                    searchResults = querySnapshot.documents.map { it.data } as MutableList<Map<String, Any>>
                    dietSearchAdapter.dietDataList.clear()
                    dietSearchAdapter.dietDataList.addAll(searchResults)
                    dietSearchAdapter.notifyDataSetChanged()
                }
            }
        setupSearchView()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //back button
        dsBack.setOnClickListener {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.framelayout_activitymain, DietPlanFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        dsSearchWeb.setOnClickListener {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val fragment = DietSearchWebFragment()

            val bundle = Bundle()
            bundle.putString("searchQuery", searchPlanView.query.toString())  // Get text from SearchView
            fragment.arguments = bundle

            fragmentTransaction.replace(R.id.framelayout_activitymain, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }
    private fun setupSearchView() {
        searchPlanView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = searchResults.filter {
                    it["dpOwnerName"]?.toString()?.contains(newText ?: "", true) == true ||
                            it["dpPlanPP"]?.toString()?.contains(newText ?: "", true) == true
                }
                dietSearchAdapter.dietDataList = filteredList as MutableList<Map<String, Any>>
                dietSearchAdapter.notifyDataSetChanged()
                return false
            }
        })
    }

}
