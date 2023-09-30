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
import androidx.viewpager.widget.ViewPager
import com.example.ihealthu.EmailStore
import com.example.ihealthu.R
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Exercise_CreateplanFragment : Fragment() {

    val db = Firebase.firestore
    private lateinit var OwnerName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_exercise__createplan, container, false)
        OwnerName = EmailStore.globalEmail.toString()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btncancel : Button = view.findViewById(R.id.btn_cancel)
        val btnimporteplan : Button = view.findViewById(R.id.btn_importeplan)
        val inputNewPlanName: EditText = view.findViewById(R.id.input_newplanname)
        val inputnewplandescription: EditText = view.findViewById(R.id.input_newplan_description)

        btnimporteplan.setOnClickListener {
            val exerciseInput: List<EditText> = listOf(
                view.findViewById(R.id.dailye1_input),
                view.findViewById(R.id.dailye2_input),
                view.findViewById(R.id.dailye3_input),
                view.findViewById(R.id.dailye4_input),
                view.findViewById(R.id.dailye5_input),
                view.findViewById(R.id.dailye6_input),
                view.findViewById(R.id.dailye7_input)
            )

            val timeInput: List<EditText> = listOf(
                view.findViewById(R.id.dailyt1_input),
                view.findViewById(R.id.dailyt2_input),
                view.findViewById(R.id.dailyt3_input),
                view.findViewById(R.id.dailyt4_input),
                view.findViewById(R.id.dailyt5_input),
                view.findViewById(R.id.dailyt6_input),
                view.findViewById(R.id.dailyt7_input)
            )

            val newPlanName = inputNewPlanName.text.toString()
            val newPlandescription = inputnewplandescription.text.toString()

            val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

            val data = hashMapOf(
                "epID" to newPlanName,
                "epDesc" to newPlandescription,
                "epOwner" to OwnerName,
                "status" to "no"
            )

            // Add a new document with epID, epDesc, and epOwner
            val newDocumentRef = db.collection("exercise").add(data)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")

                    // Now, create a subcollection for daysOfWeek under the newly created document
                    val daysCollection = documentReference.collection("DaysOfWeek")

                    for (day in daysOfWeek) {
                        val ei = exerciseInput[daysOfWeek.indexOf(day)].text.toString()
                        val et = timeInput[daysOfWeek.indexOf(day)].text.toString()

                        val exerciseData = hashMapOf(
                            "epContent" to ei,
                            "eptTime" to et
                        )

                        // Add the data for the day into the subcollection
                        daysCollection.document(day).set(exerciseData)
                            .addOnSuccessListener {
                                Log.d(TAG, "Data added for $day")
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error adding data for $day", e)
                                Toast.makeText(context, "Failed to add data for $day, try again", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                    Toast.makeText(context, "Failed to add document, try again", Toast.LENGTH_SHORT).show()
                }

            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.framelayout_activitymain, ExerciseFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
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