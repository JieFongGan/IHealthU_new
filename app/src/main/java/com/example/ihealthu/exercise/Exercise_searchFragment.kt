package com.example.ihealthu.exercise

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ihealthu.R
import com.example.ihealthu.databinding.FragmentExerciseMyplanBinding
import com.example.ihealthu.databinding.FragmentExerciseSearchBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Exercise_searchFragment : Fragment() {

    val db = Firebase.firestore

    private var _binding: FragmentExerciseSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var btn_search: ImageView
    private lateinit var input_search: EditText
    private lateinit var searchResultView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExerciseSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_search = binding.btnSearcheplan
        input_search = binding.searchEditText
        searchResultView = binding.searchResultview

        btn_search.setOnClickListener {
            performSearch(input_search.text.toString(), searchResultView)
        }
    }

    private fun performSearch(query: String, searchResultView: RecyclerView) {
        // Create a Firestore query to search for documents with matching epID
        val firestoreQuery = db.collection("exercise")
            .whereEqualTo("epID", query)

        firestoreQuery.get()
            .addOnSuccessListener { querySnapshot ->
                val searchResults = mutableListOf<Map<String, Any>>()
                for (document in querySnapshot.documents) {
                    val data = document.data
                    if (data != null) {
                        searchResults.add(data)
                    }
                }

                updateSearchResults(searchResults, searchResultView)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Search failed: ${e.message}")
            }
    }

    private fun updateSearchResults(searchResults: List<Map<String, Any>>, searchResultView: RecyclerView) {
        val searchAdapter = ExerciseSearchAdapter(searchResults)
        searchResultView.adapter = searchAdapter
        searchResultView.layoutManager = LinearLayoutManager(requireContext())
    }

}