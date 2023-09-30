package com.example.ihealthu.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.ihealthu.EmailStore
import com.example.ihealthu.Login
import com.example.ihealthu.R
import com.example.ihealthu.databinding.FragmentUserEditPersonalProfileBinding
import com.example.ihealthu.databinding.FragmentUserPersonalProfileBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID


class User_Edit_Personal_Profile : Fragment() {

    private lateinit var binding: FragmentUserEditPersonalProfileBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    // URI for the selected image
    private var selectedImageUri: Uri? = null

    companion object {
        private const val IMAGE_PICKER_REQUEST = 123
    }

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

        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        val emailToSearch = EmailStore.globalEmail
        if(emailToSearch != null){

            firestore.collection("user")
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
                        val emailTextView: TextView = view.findViewById(R.id.databaseEmail)
                        val fullNameEditText: EditText = view.findViewById(R.id.editTextFullName)
                        val genderEditText: EditText = view.findViewById(R.id.editTextGender)
                        val dateOfBirthEditText: EditText = view.findViewById(R.id.editTextDateOfBirth)

                        fullNameEditText.setText(fullName)
                        genderEditText.setText(userGender)
                        dateOfBirthEditText.setText(userDateOfBirth)
                        emailTextView.text = userEmail
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

        binding.userProfileIcon.setOnClickListener {
            openImagePicker()
        }

        binding.btnUpdate.setOnClickListener {
            updateUserProfile()
        }

        binding.btnUpdateCancel.setOnClickListener {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.framelayout_activitymain, User_Personal_Profile())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    private fun loadProfilePicture(profilePicUrl: String) {
        Glide.with(requireContext())
            .load(profilePicUrl)
            .into(binding.userProfileIcon)
    }

    // Add this function to open the image picker
    private fun openImagePicker() {
        // Create an intent to pick an image from the gallery
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICKER_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                selectedImageUri = it

                // Set the selected image to the user's profile picture
                binding.userProfileIcon.setImageURI(selectedImageUri)
            }
        }
    }

    // Function to update the user profile
    private fun updateUserProfile() {
        val emailToSearch = EmailStore.globalEmail

        if (emailToSearch != null) {
            val fullName = binding.editTextFullName.text.toString()
            val gender = binding.editTextGender.text.toString()
            val dateOfBirth = binding.editTextDateOfBirth.text.toString()

            // Upload the selected image to Firebase Storage and update Firestore document
            if (selectedImageUri != null) {
                uploadPhotoAndUpdateFirestore(emailToSearch, fullName, gender, dateOfBirth)
            } else {
                // If no new image is selected, update Firestore without changing the profile pic
                updateFirestore(emailToSearch, fullName, gender, dateOfBirth, null)
            }
        }
    }

    // Function to upload the selected image to Firebase Storage and update Firestore
    private fun uploadPhotoAndUpdateFirestore(
        emailToSearch: String,
        fullName: String,
        gender: String,
        dateOfBirth: String
    ) {
        // Generate a unique name for the profile picture file
        val profilePicFileName = UUID.randomUUID().toString()
        val profilePicRef = storage.reference.child("profile_pics/$profilePicFileName")

        // Upload the selected image to Firebase Storage
        profilePicRef.putFile(selectedImageUri!!)
            .addOnSuccessListener { _ ->
                // Get the download URL of the uploaded image
                profilePicRef.downloadUrl
                    .addOnSuccessListener { uri ->
                        // Update Firestore with the new user data including the profile pic URL
                        updateFirestore(emailToSearch, fullName, gender, dateOfBirth, uri.toString())
                    }
                    .addOnFailureListener { exception ->
                        // Handle any errors related to getting the download URL
                        Toast.makeText(requireContext(), "Failed to get image URL.", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener { exception ->
                // Handle any errors related to uploading the image
                Toast.makeText(requireContext(), "Failed to upload image.", Toast.LENGTH_SHORT).show()
            }
    }

    // Function to update Firestore with user data
    private fun updateFirestore(
        emailToSearch: String,
        fullName: String,
        gender: String,
        dateOfBirth: String,
        profilePicUrl: String?
    ) {
        // Update Firestore document fields
        firestore.collection("user")
            .whereEqualTo("email", emailToSearch)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val document =
                        documents.documents[0] // Assuming there's only one user with the specified email

                    val userId = document.id

                    val newData = hashMapOf(
                        "dateOfBirth" to dateOfBirth,
                        "fullname" to fullName,
                        "gender" to gender
                    )

                    if (profilePicUrl != null) {
                        newData["profilepic"] = profilePicUrl
                    }

                    // Update the user document in Firestore
                    firestore.collection("user")
                        .document(userId)
                        .update(newData as Map<String, Any>)
                        .addOnSuccessListener {

                            Toast.makeText(
                                requireContext(),
                                "Update Successful",
                                Toast.LENGTH_SHORT
                            ).show()

                            val fragmentManager = parentFragmentManager
                            val fragmentTransaction = fragmentManager.beginTransaction()
                            fragmentTransaction.replace(
                                R.id.framelayout_activitymain,
                                User_Personal_Profile()
                            )
                            fragmentTransaction.addToBackStack(null)
                            fragmentTransaction.commit()
                        }
                        .addOnFailureListener { exception ->
                            // Log the error for debugging
                            Log.e("Firestore Update Error", exception.toString())

                            Toast.makeText(
                                requireContext(),
                                "Failed to update data, try again",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { exception ->
                // Log the error for debugging
                Log.e("Firestore Query Error", exception.toString())

                Toast.makeText(
                    requireContext(),
                    "Failed to fetch user data, try again",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}
