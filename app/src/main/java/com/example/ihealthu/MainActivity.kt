package com.example.ihealthu

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ihealthu.databinding.ActivityMainBinding
import com.example.ihealthu.diet.DietPlanFragment
import com.example.ihealthu.exercise.ExerciseFragment
import com.example.ihealthu.home.HomeFragment
import com.example.ihealthu.profile.MainViewModel
import com.example.ihealthu.profile.User_Main
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        replaceFragment(HomeFragment(), "home_fragment")

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> replaceFragment(HomeFragment(), "home_fragment")
                R.id.nav_diet -> replaceFragment(DietPlanFragment(), "dietplan_fragment")
                R.id.nav_exercise -> replaceFragment(ExerciseFragment(), "exercise_fragment")
                R.id.nav_profile -> replaceFragment(User_Main(), "user_main")

                else ->{

                }
            }
            true
        }

        if (savedInstanceState == null && mainViewModel.currentFragmentTag != null) {
            // Restore the previous fragment based on the saved tag
            val restoredTag = mainViewModel.currentFragmentTag
            val fragment = supportFragmentManager.findFragmentByTag(restoredTag)
            if (fragment != null) {
                replaceFragment(fragment, restoredTag as String)
            }
        }
    }

    private fun replaceFragment(fragment: Fragment, tag: String) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.framelayout_activitymain, fragment, tag)
        fragmentTransaction.commit()
        mainViewModel.currentFragmentTag = tag
    }
}