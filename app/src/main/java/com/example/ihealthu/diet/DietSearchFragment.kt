package com.example.ihealthu.diet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class DietSearchFragment : Fragment() {
    private var _binding: FragmentDietSearchBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private lateinit var searchPlanView: SearchView
    private lateinit var dsListView: ListView
    // Initialize your adapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDietSearchBinding.inflate(inflater, container, false)
        searchPlanView = binding.searchPlanView
        dsListView = binding.dsListView
        //getdata form firestore
        var searchResults = mutableListOf<Map<String, Any>>()
        //listview item click

        //set up adapter
//        val adapter = object : ListView.Adapter<RecyclerView.ViewHolder>() {
//            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//                val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
//                return object : RecyclerView.ViewHolder(view) {}
//            }
//
//            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//                val textView = holder.itemView as TextView
//                textView.text = searchResults[position]["dpOwnerName"] as? String ?: "N/A"
//            }
//
//            override fun getItemCount(): Int {
//                return searchResults.size
//            }
//        }


        dsListView.isClickable = true
//        dsListView.adapter = adapter
        dsListView.setOnItemClickListener{ parent,view,position,id ->
//            val dsOwnerName
        }

        // Set up searchPlanView*search bar listener
//        searchPlanView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return true
//            }
//            override fun onQueryTextChange(newText: String?): Boolean {
//                searchFirestore(newText){ results ->
//                    searchResults = results.toMutableList()
//                    adapter.notifyDataSetChanged()
//                }
//                return true
//            }
//        })
        return binding.root
    }

    private fun searchFirestore(query: String?, onResults: (List<Map<String, Any>>) -> Unit) {
        if (query == null || query.isEmpty()) {
            onResults(emptyList())
            return
        }
        db.collection("diet")
            .whereEqualTo("dpOwnerName", query)
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                val results = querySnapshot?.map { it.data } ?: emptyList()
                onResults(results)
            }
    }
}
