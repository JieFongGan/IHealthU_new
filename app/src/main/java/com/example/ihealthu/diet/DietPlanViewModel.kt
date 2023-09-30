package com.example.ihealthu.diet

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DietPlanViewModel : ViewModel() {
    val dietData = MutableLiveData<List<Map<String, Any>>>()
    val daySelected = MutableLiveData<String>()
    val db = Firebase.firestore

    fun fetchDataFromFirestore(day: String, ownerName: String) {
        viewModelScope.launch {
            val data = fetchData(day, ownerName)
            dietData.postValue(data)
            daySelected.postValue(day)
        }
    }

    private suspend fun fetchData(day: String, ownerName: String): List<Map<String, Any>> {
        return withContext(Dispatchers.IO) {
            val dietDataList = mutableListOf<Map<String, Any>>()
            try {
                val querySnapshot = db.collection("diet")
                    .whereEqualTo("dpDietDays", day)
                    .whereEqualTo("dpOwnerName", ownerName)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        for (document in querySnapshot) {
                            for (document in querySnapshot.documents) {
                                Log.d("Firestore", "Document ID: ${document.id} => Document Data: ${document.data}")
                                val data = document.data
                                if (data != null) {
                                    dietDataList.add(data)
                                }
                            }
                        }
                    }.await()
            } catch (e: Exception) {
                Log.e("DietPlanFragment", "Error fetching data: ${e.message}")
            }
            return@withContext dietDataList
        }
    }
}
