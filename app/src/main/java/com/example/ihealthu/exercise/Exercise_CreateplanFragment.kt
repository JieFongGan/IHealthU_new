package com.example.ihealthu.exercise

import Exercise_MyplanFragment
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
import androidx.viewpager.widget.ViewPager
import com.example.ihealthu.R
import com.google.android.material.tabs.TabLayout
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

        btnimporteplan.setOnClickListener {
            val exerciseInput1: EditText = view.findViewById(R.id.dailye1_input)
            val timeInput1: EditText = view.findViewById(R.id.dailyt1_input)
            val exerciseInput2: EditText = view.findViewById(R.id.dailye2_input)
            val timeInput2: EditText = view.findViewById(R.id.dailyt2_input)
            val exerciseInput3: EditText = view.findViewById(R.id.dailye3_input)
            val timeInput3: EditText = view.findViewById(R.id.dailyt3_input)
            val exerciseInput4: EditText = view.findViewById(R.id.dailye4_input)
            val timeInput4: EditText = view.findViewById(R.id.dailyt4_input)
            val exerciseInput5: EditText = view.findViewById(R.id.dailye5_input)
            val timeInput5: EditText = view.findViewById(R.id.dailyt5_input)
            val exerciseInput6: EditText = view.findViewById(R.id.dailye6_input)
            val timeInput6: EditText = view.findViewById(R.id.dailyt6_input)
            val exerciseInput7: EditText = view.findViewById(R.id.dailye7_input)
            val timeInput7: EditText = view.findViewById(R.id.dailyt7_input)

            val newPlanName = inputNewPlanName.text.toString()
            val newPlandescription = inputnewplandescription.text.toString()
            val ei1 = exerciseInput1.text.toString()
            val et1 = timeInput1.text.toString()

            val ei2 = exerciseInput2.text.toString()
            val et2 = timeInput2.text.toString()

            val ei3 = exerciseInput3.text.toString()
            val et3 = timeInput3.text.toString()

            val ei4 = exerciseInput4.text.toString()
            val et4 = timeInput4.text.toString()

            val ei5 = exerciseInput5.text.toString()
            val et5 = timeInput5.text.toString()

            val ei6 = exerciseInput6.text.toString()
            val et6 = timeInput6.text.toString()

            val ei7 = exerciseInput7.text.toString()
            val et7 = timeInput7.text.toString()

            val data = hashMapOf(
                "epID" to newPlanName,
                "epDesc" to newPlandescription,
                "epOwner" to "jian",
                "epMon" to ei1,
                "eptMon" to et1,
                "epTue" to ei2,
                "eptTue" to et2,
                "epWed" to ei3,
                "eptWed" to et3,
                "epThu" to ei4,
                "eptThu" to et4,
                "epFri" to ei5,
                "eptFri" to et5,
                "epSat" to ei6,
                "eptSat" to et6,
                "epSun" to ei7,
                "eptSun" to et7
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
        btncancel.setOnClickListener {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.framelayout_activitymain, Exercise_MyplanFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }
}