package com.example.ihealthu.diet

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.ihealthu.EmailStore
import com.example.ihealthu.MainActivity
import com.example.ihealthu.R
import com.example.ihealthu.databinding.FragmentDietPlanAddBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class DietPlanAddFragment : Fragment() {
    private var _binding: FragmentDietPlanAddBinding? = null
    private val binding get() = _binding!!
    private lateinit var etPlanPP: EditText
    private lateinit var etBftime: EditText
    private lateinit var etBfkals: EditText
    private lateinit var etBfRemark: EditText
    private lateinit var etLutime: EditText
    private lateinit var etLukals: EditText
    private lateinit var etLuRemark: EditText
    private lateinit var etDntime: EditText
    private lateinit var etDnkals: EditText
    private lateinit var etDnRemark: EditText

    private lateinit var etOwnerName: String
    private lateinit var etDietDays: String

    private lateinit var comfirmAdd: Button
    private lateinit var cancelAdd: Button

    private var documentId: String?=null
    private var dataForTheDayf: List<Map<String, Any>>?=null

    val db = Firebase.firestore


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDietPlanAddBinding.inflate(inflater, container, false)

        comfirmAdd = binding.confirmPlanAdd
        cancelAdd = binding.cancelPlanAdd

        etPlanPP = binding.editTextPlanPurpose
        etBftime = binding.editTextBreakfastTime
        etBfkals = binding.editTextBreakfastKals
        etBfRemark = binding.editTextBreakfastRemark
        etLutime = binding.editTextLunchTime
        etLukals = binding.editTextLunchKals
        etLuRemark = binding.editTextLunchRemark
        etDntime = binding.editTextDinnerTime
        etDnkals = binding.editTextDinnerKals
        etDnRemark = binding.editTextDinnerRemark

        etOwnerName = EmailStore.globalEmail.toString()
        //get value for weekly day
        parentFragmentManager.setFragmentResultListener("selectedDay", this) { _, bundle ->
            val selectedDay = bundle.getString("day")
            etDietDays = selectedDay.toString()
        }
        //get the existing data documentID *no use now
        parentFragmentManager.setFragmentResultListener("documentId", this) { _, bundle ->
            val documentIds = bundle.getString("docID")
            documentId = documentIds.toString()
        }
        //ettexthold to existing data !!
        parentFragmentManager.setFragmentResultListener("dietPlanData", this) { _, bundle ->
            val dataForTheDay = bundle.getSerializable("dayData") as? List<Map<String, Any>>
            dataForTheDayf = dataForTheDay
            if (dataForTheDay != null && dataForTheDay.isNotEmpty()) {
                for ((index, dataMap) in dataForTheDay.withIndex()) {
                    Log.d("Debug", "test Map value at index $index: $dataMap")
                }
                val firstData = dataForTheDay.first()
                etPlanPP.setText(firstData["dpPlanPP"] as? String)
                etBftime.setText(firstData["dpBftime"] as? String)
                etBfkals.setText(firstData["dpBfkals"] as? String)
                etBfRemark.setText(firstData["dpBfRemark"] as? String)
                etLutime.setText(firstData["dpLutime"] as? String)
                etLukals.setText(firstData["dpLukals"] as? String)
                etLuRemark.setText(firstData["dpLuRemark"] as? String)
                etDntime.setText(firstData["dpDntime"] as? String)
                etDnkals.setText(firstData["dpDnkals"] as? String)
                etDnRemark.setText(firstData["dpDnRemark"] as? String)
        }
        }
        //add data xxx!!!!!!!!!!!!!!
        comfirmAdd.setOnClickListener{
            try {
                Log.d("Debug", "comfirmAdd got run")
                val txplanPP = etPlanPP.text.toString()
                val txBftime = etBftime.text.toString()
                val txBfkals = etBfkals.text.toString()
                val txBfRemark = etBfRemark.text.toString()
                val txLutime = etLutime.text.toString()
                val txLukals = etLukals.text.toString()
                val txLuRemark = etLuRemark.text.toString()
                val txDntime = etDntime.text.toString()
                val txDnkals = etDnkals.text.toString()
                val txDnRemark = etDnRemark.text.toString()

                val txOwnerName = etOwnerName
                val txDietDays = etDietDays

                //input vali
                if (txplanPP.isEmpty()) {
                    Toast.makeText(context, "Plan Purpose cannot be empty", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val txBfkalsInt = txBfkals.toIntOrNull()
                val txLukalsInt = txLukals.toIntOrNull()
                val txDnkalsInt = txDnkals.toIntOrNull()

                if (txBfkalsInt != null && txBfkalsInt > 3000) {
                    Toast.makeText(context, "Breakfast of over 3,000 calories seems unusual", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (txLukalsInt != null && txLukalsInt > 3000) {
                    Toast.makeText(context, "Lunch of over 3,000 calories seems unusual", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (txDnkalsInt != null && txDnkalsInt > 3000) {
                    Toast.makeText(context, "Dinner of over 3,000 calories seems unusual", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                //end input vali

                val data = hashMapOf(
                    "dpOwnerName" to txOwnerName,
                    "dpDietDays" to txDietDays,
                    "dpPlanPP" to txplanPP,
                    "dpBftime" to txBftime,
                    "dpBfkals" to txBfkals,
                    "dpBfRemark" to txBfRemark,
                    "dpLutime" to txLutime,
                    "dpLukals" to txLukals,
                    "dpLuRemark" to txLuRemark,
                    "dpDntime" to txDntime,
                    "dpDnkals" to txDnkals,
                    "dpDnRemark" to txDnRemark
                )
                    var ownerName: String? = null
                    var day: String? = null
                    if (dataForTheDayf != null && dataForTheDayf!!.isNotEmpty()) {
                        for ((index, dataMap) in dataForTheDayf!!.withIndex()) {
                            Log.d("Debug", "test Map value at index $index: $dataMap")
                            ownerName = dataMap["dpOwnerName"] as? String
                            day = dataMap["dpDietDays"] as? String
                        }
                        Log.d("testvalue", "test value ownername = $ownerName , $day")
                    }
                    if(dataForTheDayf != null && dataForTheDayf!!.isNotEmpty() && ownerName!=null && day!=null){
                        // Update existing data
                        db.collection("diet")
                            .whereEqualTo("dpOwnerName", ownerName)
                            .whereEqualTo("dpDietDays", day)
                            .get()
                            .addOnSuccessListener {querySnapshot ->
                                if (querySnapshot.documents.isNotEmpty()) {
                                    val document = querySnapshot.documents[0]
                                    db.collection("diet")
                                        .document(document.id)
                                        .set(data)
                                        .addOnSuccessListener {
                                            Toast.makeText(context,"data updated",Toast.LENGTH_SHORT).show()
                                            val fragmentManager = parentFragmentManager
                                            val fragmentTransaction = fragmentManager.beginTransaction()
                                            fragmentTransaction.replace(
                                                R.id.framelayout_activitymain,
                                                DietPlanFragment()
                                            )
                                            fragmentTransaction.addToBackStack(null)
                                            fragmentTransaction.commit()}
                                }
                            }.addOnFailureListener { e ->
                                Log.w(ContentValues.TAG, "Error adding document", e)
                                Toast.makeText(context, "updated failed, try again", Toast.LENGTH_SHORT).show()
                            }
                    }else{
                        db.collection("diet")
                            .add(data)
                            .addOnSuccessListener { documentReference ->
                                Toast.makeText(context,"data saved",Toast.LENGTH_SHORT).show()
                                Log.d(
                                    ContentValues.TAG,
                                    "DocumentSnapshot added with ID: ${documentReference.id}"
                                )
                                val fragmentManager = parentFragmentManager
                                val fragmentTransaction = fragmentManager.beginTransaction()
                                fragmentTransaction.replace(
                                    R.id.framelayout_activitymain,
                                    DietPlanFragment()
                                )
                                fragmentTransaction.addToBackStack(null)
                                fragmentTransaction.commit()
                            }
                            .addOnFailureListener { e ->
                                Log.w(ContentValues.TAG, "Error adding document", e)
                                Toast.makeText(context, "failed, try again", Toast.LENGTH_SHORT).show()
                            }
                    }
            }catch(e: Exception){
                Log.e("FirestoreError", "Exception: ", e)
            }
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //cancel button
            cancelAdd.setOnClickListener {
                Toast.makeText(view.context,"canceled",Toast.LENGTH_SHORT).show()
                val fragmentManager = parentFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.framelayout_activitymain, DietPlanFragment())
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}