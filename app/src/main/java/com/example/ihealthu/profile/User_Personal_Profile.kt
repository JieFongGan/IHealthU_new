package com.example.ihealthu.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.ihealthu.EmailStore
import com.example.ihealthu.R
import com.example.ihealthu.databinding.FragmentUserPersonalProfileBinding
import com.google.firebase.firestore.FirebaseFirestore


class User_Personal_Profile : Fragment() {
    private lateinit var binding: FragmentUserPersonalProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserPersonalProfileBinding.inflate(inflater, container, false)
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
                        val profilePicUrl = document["profilepic"] as? String

                        // Update TextViews with user data
                        val fullNameTextView: TextView = view.findViewById(R.id.databaseFullName)
                        val emailTextView: TextView = view.findViewById(R.id.databaseEmail)
                        val genderTextView: TextView = view.findViewById(R.id.databaseGender)
                        val dateOfBirthTextView: TextView = view.findViewById(R.id.databaseDateOfBirth)

                        fullNameTextView.text = fullName
                        emailTextView.text = userEmail
                        genderTextView.text = userGender
                        dateOfBirthTextView.text = userDateOfBirth
                        // Load and display profile picture if available
                        if (!profilePicUrl.isNullOrEmpty()) {
                            loadProfilePicture(profilePicUrl)
                        }

                    } else {
                        Toast.makeText(requireContext(), "No user found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "failed, try again", Toast.LENGTH_SHORT).show()
                }
        }

        binding.btnEdit.setOnClickListener {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.framelayout_activitymain, User_Edit_Personal_Profile())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }



        binding.btnBack.setOnClickListener {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.framelayout_activitymain, User_Main())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    private fun loadProfilePicture(profilePicUrl: String) {
        Glide.with(requireContext())
            .load(profilePicUrl)
            .into(binding.userProfileIcon)
    }

}