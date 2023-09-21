package com.example.ihealthu.exercise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.example.ihealthu.R

class Exercise_MyplanFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_exercise_myplan, container, false)

        // Set up the Toolbar
        val toolbar = view.findViewById<Toolbar>(R.id.toolbaremp)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Your Toolbar Title"

        // Handle the back button press
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
            // Example: findNavController().navigateUp()
        }

        return view
    }
}