package com.example.ihealthu.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.example.ihealthu.R


class User_BMI_Main : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user__b_m_i__main, container, false)

        val button1: RelativeLayout = view.findViewById(R.id.user_calculateBmi)

        button1.setOnClickListener {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.framelayout_activitymain, User_BMI())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        val button2: RelativeLayout = view.findViewById(R.id.user_viewBmiRecord)

        button2.setOnClickListener {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.framelayout_activitymain, User_Added_BMI())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        // Inflate the layout for this fragment
        return view
    }


}