package com.example.ihealthu.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ihealthu.R

class HomeAdapter(
    private val exerciseList: MutableList<Map<String, Any>>,
    private val selectedDay: String
):    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val goalhour: TextView = itemView.findViewById(R.id.goal_hour)
        val goalexercise: TextView = itemView.findViewById(R.id.goal_exercise)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_dailyexercise, parent, false)
        return HomeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val currentItem = exerciseList[position]

        val epContent = currentItem["epContent"]?.toString() ?: "relax and chill"
        val epTime = currentItem["eptTime"]?.toString() ?: "No exercise today"

        holder.goalhour.text = epTime
        holder.goalexercise.text = epContent
    }

    fun submitList(newData: List<Map<String, Any>>) {
        exerciseList.clear()
        exerciseList.addAll(newData)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return exerciseList.size
    }
}