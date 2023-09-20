package com.example.ihealthu.diet

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class DietOnGoPagerAdapter (fm:FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {
    private val weekList = listOf("Mon","Tue","Wed","Thu","Fri","Sat","Sun")
    override fun getItemCount(): Int {
        return weekList.size
    }

    override fun createFragment(position: Int): Fragment {
//        val bundle = Bundle()
//        bundle.putString("weekday", weekList[position])
        return DietCreateFragment()
    }

//    override fun createFragment(position: Int): Fragment {
//        // 根据位置创建对应星期的Fragment
//        return when (position) {
//            0 -> createWeekdayFragment("Mon")
//            1 -> createWeekdayFragment("Tue")
//            2 -> createWeekdayFragment("Wed")
//            3 -> createWeekdayFragment("Thu")
//            4 -> createWeekdayFragment("Fri")
//            5 -> createWeekdayFragment("Sat")
//            6 -> createWeekdayFragment("Sun")
//            else -> createWeekdayFragment("Mon") // 默认显示星期一的内容
//        }
//    }
//
//    private fun createWeekdayFragment(weekday: String): Fragment {
//        // 根据传入的星期创建对应的 Fragment
//        val fragment = DietOnGoFragment()
//        val bundle = Bundle()
//        bundle.putString("weekday", weekday)
//        fragment.arguments = bundle
//        return fragment
//    }

}