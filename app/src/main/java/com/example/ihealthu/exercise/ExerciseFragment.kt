package com.example.ihealthu.exercise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.ihealthu.R

class ExerciseFragment : Fragment() {

//    private lateinit var exercisePlanDatabase: ExercisePlanDatabase
//    private lateinit var exercisePlanDao: ExercisePlanDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_exercise, container, false)
//        exercisePlanDatabase = ExercisePlanDatabase.getInstance(requireContext())
//        exercisePlanDao = exercisePlanDatabase.exercisePlanDao()

        // Rest of your onCreateView code
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btntomyeplan : Button = view.findViewById(R.id.btn_myeplan)
        val btntosearcheplan : Button = view.findViewById(R.id.btn_searcheplan)


        btntomyeplan.setOnClickListener {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.framelayout_activitymain, Exercise_MyplanFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        btntosearcheplan.setOnClickListener {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.framelayout_activitymain, Exercise_searchFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

}
