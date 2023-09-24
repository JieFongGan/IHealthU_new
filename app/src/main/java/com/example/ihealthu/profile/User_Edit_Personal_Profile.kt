package com.example.ihealthu.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.ihealthu.EmailStore
import com.example.ihealthu.Login
import com.example.ihealthu.R
import com.example.ihealthu.databinding.FragmentUserEditPersonalProfileBinding
import com.example.ihealthu.databinding.FragmentUserPersonalProfileBinding
import com.google.firebase.firestore.FirebaseFirestore


class User_Edit_Personal_Profile : Fragment() {

    private lateinit var binding: FragmentUserEditPersonalProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserEditPersonalProfileBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = FirebaseFirestore.getInstance()

        val emailToSearch = EmailStore.globalEmail
        if(emailToSearch != null){

            db.collection("user")
                .whereEqualTo("email", emailToSearch)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val document = documents.documents[0] // Assuming there's only one user with the specified email

                        // Retrieve user data
                        val fullName = document.getString("fullname")
                        val userEmail = document.getString("email")
                        val userGender = document.getString("gender")
                        val userDateOfBirth = document.getString("dateOfBirth")

                        // Update TextViews with user data
                        val emailTextView: TextView = view.findViewById(R.id.databaseEmail)
                        val fullNameEditText: EditText = view.findViewById(R.id.editTextFullName)
                        val genderEditText: EditText = view.findViewById(R.id.editTextGender)
                        val dateOfBirthEditText: EditText = view.findViewById(R.id.editTextDateOfBirth)

                        fullNameEditText.setText(fullName)
                        genderEditText.setText(userGender)
                        dateOfBirthEditText.setText(userDateOfBirth)
                        emailTextView.text = userEmail

                    } else {
                        Toast.makeText(requireContext(), "No user found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "failed, try again", Toast.LENGTH_SHORT).show()
                }
        }

        binding.btnUpdate.setOnClickListener {
            val db = FirebaseFirestore.getInstance()

            val emailToSearch = EmailStore.globalEmail

            val newFullNameEditText = binding.editTextFullName.text.toString()
            val newGenderEditText = binding.editTextGender.text.toString()
            val newDateOfBirthEditText = binding.editTextDateOfBirth.text.toString()

            db.collection("user")
                .whereEqualTo("email", emailToSearch)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val document = documents.documents[0] // Assuming there's only one user with the specified email

                        val userId = document.id

                        val newData = hashMapOf(
                            "dateOfBirth" to newDateOfBirthEditText,
                            "fullname" to newFullNameEditText,
                            "gender" to newGenderEditText
                        )

                        // Update the user document in Firestore
                        db.collection("user")
                            .document(userId)
                            .update(newData as Map<String, Any>)
                            .addOnSuccessListener {
                                val fragmentManager = parentFragmentManager
                                val fragmentTransaction = fragmentManager.beginTransaction()
                                fragmentTransaction.replace(R.id.framelayout_activitymain, User_Personal_Profile())
                                fragmentTransaction.addToBackStack(null)
                                fragmentTransaction.commit()
                            }
                            .addOnFailureListener { exception ->
                                // Log the error for debugging
                                Log.e("Firestore Update Error", exception.toString())

                                Toast.makeText(requireContext(), "Failed to update data, try again", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener { exception ->
                    // Log the error for debugging
                    Log.e("Firestore Query Error", exception.toString())

                    Toast.makeText(requireContext(), "Failed to fetch user data, try again", Toast.LENGTH_SHORT).show()
                }
        }

        binding.btnUpdateCancel.setOnClickListener {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.framelayout_activitymain, User_Personal_Profile())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

}
