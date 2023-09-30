package com.example.ihealthu.exercise

import MyeplanAdapter
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ihealthu.EmailStore
import com.example.ihealthu.R
import com.example.ihealthu.databinding.FragmentExerciseMyplanBinding
import com.example.ihealthu.exercise.Exercise_CreateplanFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Exercise_MyplanFragment : Fragment() {

    private val db = Firebase.firestore
    private var _binding: FragmentExerciseMyplanBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnneweplan: Button
    private lateinit var OwnerName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExerciseMyplanBinding.inflate(inflater, container, false)
        recyclerView = binding.MyplanView
        btnneweplan = binding.btnNeweplan
        OwnerName = EmailStore.globalEmail.toString()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val myeplanAdapter = MyeplanAdapter(
            requireContext(),
            parentFragmentManager,
            emptyList<Map<String, Any>>().toMutableList(),
            this::deleteItem,
            this::updateStatusInFirestore
        )

        recyclerView.adapter = myeplanAdapter

        btnneweplan.setOnClickListener {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.framelayout_activitymain, Exercise_CreateplanFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        fetchDataFromFirestore(OwnerName) { data ->
            myeplanAdapter.submitList(data)
        }
    }

    private fun updateStatusInFirestore(epID: String) {
        val collectionName = "exercise"

        db.collection(collectionName)
            .whereEqualTo("epID", epID)
            .whereEqualTo("epOwner", OwnerName)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]

                    val currentStatus = document.getString("status")

                    val newStatus = if (currentStatus == "Yes") "No" else "Yes"

                    // Update the "status" field in Firestore with the new value
                    document.reference.update("status", newStatus)
                        .addOnSuccessListener {
                            Log.d(TAG, "Status updated successfully to $newStatus")
                            val fragmentManager = parentFragmentManager
                            val fragmentTransaction = fragmentManager.beginTransaction()
                            fragmentTransaction.replace(R.id.framelayout_activitymain, Exercise_MyplanFragment())
                            fragmentTransaction.addToBackStack(null)
                            fragmentTransaction.commit()
                        }
                        .addOnFailureListener { e ->
                            Log.e(TAG, "Status update failed: ${e.message}")
                            Toast.makeText(requireContext(), "Status update failed: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Log.d(TAG, "No document found with epID: $epID")
                    // Handle the case where no document with the specified epID is found
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Query failed: ${e.message}")
                Toast.makeText(requireContext(), "Query failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteItem(position: Int) {
        val item = (recyclerView.adapter as MyeplanAdapter).getItemAtPosition(position)
        val epID = item["epID"].toString()

        Log.d(TAG, "Deleting document with epID: $epID")

        db.collection("exercise")
            .whereEqualTo("epID", epID)
            .whereEqualTo("epOwner", OwnerName)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    db.collection("exercise")
                        .document(document.id)
                        .delete()
                        .addOnSuccessListener {
                            Log.d(TAG, "Document deleted successfully")
                            val fragmentManager = parentFragmentManager
                            val fragmentTransaction = fragmentManager.beginTransaction()
                            fragmentTransaction.replace(R.id.framelayout_activitymain, Exercise_MyplanFragment())
                            fragmentTransaction.addToBackStack(null)
                            fragmentTransaction.commit()
                        }
                        .addOnFailureListener { e ->
                            Log.e(TAG, "Delete failed: ${e.message}")
                            Toast.makeText(requireContext(), "Delete failed: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Query failed: ${e.message}")
                Toast.makeText(requireContext(), "Query failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchDataFromFirestore(ownerName: String, onDataFetched: (List<Map<String, Any>>) -> Unit) {
        val MyeplanList = mutableListOf<Map<String, Any>>()
        try {
            val collectionName = "exercise"

            db.collection(collectionName)
                .whereEqualTo("epOwner", ownerName)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot.documents) {
                        val data = document.data
                        if (data != null) {
                            MyeplanList.add(data)
                        }
                    }
                    onDataFetched(MyeplanList)
                }
                .addOnFailureListener { e ->
                    Log.e("MyePlanFragment", "Error fetching data: ${e.message}")
                }
        } catch (e: Exception) {
            Log.e("MyePlanFragment", "Error fetching data: ${e.message}")
        }
    }
}
