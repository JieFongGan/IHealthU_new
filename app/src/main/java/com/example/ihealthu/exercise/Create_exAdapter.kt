package com.example.ihealthu.exercise

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class Create_exAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        // Return the appropriate fragment for each "page"
        return when (position) {
            0 -> Create_exmon()
            1 -> Create_extue()
            2 -> Create_exwed()
            3 -> Create_exthu()
            4 -> Create_exfri()
            5 -> Create_exsat()
            6 -> Create_exsun()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

    override fun getCount(): Int {
        // Return the number of "pages"
        return 7
    }

    override fun getPageTitle(position: Int): CharSequence? {
        // Return the title for each "page" (e.g., day of the week)
        return when (position) {
            0 -> "Monday"
            1 -> "Tuesday"
            2 -> "Wednesday"
            3 -> "Thuesday"
            4 -> "Friday"
            5 -> "Saturday"
            6 -> "Sunday"
            else -> ""
        }
    }
}