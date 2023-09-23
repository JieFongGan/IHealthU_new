package com.example.ihealthu.exercise

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.ihealthu.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class Exercise_CreateplanFragment : Fragment() {

    val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_exercise__createplan, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btncancel : Button = view.findViewById(R.id.btn_cancel)
        val btnimporteplan : Button = view.findViewById(R.id.btn_importeplan)
        val inputNewPlanName: EditText = view.findViewById(R.id.input_newplanname)
        val inputnewplandescription: EditText = view.findViewById(R.id.input_newplan_description)


        btncancel.setOnClickListener {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.framelayout_activitymain, Exercise_MyplanFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        btnimporteplan.setOnClickListener {

            val newPlanName = inputNewPlanName.text.toString()
            val newPlandescription = inputnewplandescription.text.toString()

            val data = hashMapOf(
                "epID" to newPlanName,
                "epDesc" to newPlandescription
            )

            db.collection("exercise")
                .add(data)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    val fragmentManager = parentFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.framelayout_activitymain, ExerciseFragment())
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                    Toast.makeText(context, "failed, try again", Toast.LENGTH_SHORT).show()
                }
        }
    }
}