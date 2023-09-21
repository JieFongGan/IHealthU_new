package com.example.ihealthu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.example.ihealthu.databinding.ActivityMainBinding
import com.example.ihealthu.diet.DietFragment
import com.example.ihealthu.exercise.Empty_Exerciseholder
import com.example.ihealthu.exercise.ExerciseFragment
import com.example.ihealthu.home.HomeFragment
import com.example.ihealthu.profile.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> replaceFragment(HomeFragment())
                R.id.nav_diet -> replaceFragment(DietFragment())
                R.id.nav_exercise -> replaceFragment(Empty_Exerciseholder())
                R.id.nav_profile -> replaceFragment(ProfileFragment())

                else ->{

                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.framelayout_activitymain,fragment)
        fragmentTransaction.commit()
    }
}