package com.example.ihealthu.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.example.ihealthu.Login
import com.example.ihealthu.R
import com.example.ihealthu.databinding.FragmentUserMainBinding
import com.google.firebase.auth.FirebaseAuth


class User_Main : Fragment() {
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_main, container, false)

        val button1: RelativeLayout = view.findViewById(R.id.user_personal_profile)

        button1.setOnClickListener {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.framelayout_activitymain, User_Personal_Profile())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        val button2: RelativeLayout = view.findViewById(R.id.user_weight)

        button2.setOnClickListener {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.framelayout_activitymain, User_Weight())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        val button3: RelativeLayout = view.findViewById(R.id.user_review_comment)

        button3.setOnClickListener {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.framelayout_activitymain, User_Review_Comment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        val button4: RelativeLayout = view.findViewById(R.id.user_logOut)

        button4.setOnClickListener {
            // Create and configure the AlertDialog
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Sign Out")
            builder.setMessage("Are you sure you want to sign out?")

            // Add a "Yes" button
            builder.setPositiveButton("Yes") { dialog, which ->
                // User clicked "Yes," sign out
                firebaseAuth.signOut()

                // Navigate to the login screen
                val intent = Intent(requireContext(), Login::class.java)
                startActivity(intent)

                dialog.dismiss() // Dismiss the dialog
            }

            // Add a "No" button
            builder.setNegativeButton("No") { dialog, which ->
                // User clicked "No," do nothing and dismiss the dialog
                dialog.dismiss()
            }

            // Create and show the AlertDialog
            val dialog = builder.create()
            dialog.show()
        }

        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }

}