package com.example.ihealthu.exercise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.ihealthu.R
import com.example.ihealthu.databinding.FragmentExerciseBinding

class ExerciseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_exercise, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btntomyeplan : Button = view.findViewById(R.id.btn_myeplan)
        val btntosearcheplan : Button = view.findViewById(R.id.btn_searcheplan)


        btntomyeplan.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_exerciseFragment_to_exercise_MyplanFragment)
        }

        btntosearcheplan.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_exerciseFragment_to_exercise_searchFragment)
        }


    }
}
