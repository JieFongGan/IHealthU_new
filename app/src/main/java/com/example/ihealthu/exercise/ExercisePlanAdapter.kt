package com.example.ihealthu.exercise

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ihealthu.R
import com.example.ihealthu.diet.DietPlanAdapter

class ExercisePlanAdapter(private val exerciseplanlist: MutableList<Map<String, Any>>,
                          private val selectedDay: String
) : RecyclerView.Adapter<ExercisePlanAdapter.ExerciseViewHolder>() {

    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val goalexercise: TextView = itemView.findViewById(R.id.goal_exercise)
        val goalhour: TextView = itemView.findViewById(R.id.goal_hour)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExercisePlanAdapter.ExerciseViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_dailyexercise, parent, false)
        return ExerciseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val currentItem = exerciseplanlist[position]

        holder.goalhour.text = currentItem["ept$selectedDay"]?.toString() ?: "N/A"
        holder.goalexercise.text = currentItem["ep$selectedDay"]?.toString() ?: "N/A"
        Log.d("DietPlanFragment", "fk $selectedDay")
    }

    fun submitList(newData: List<Map<String, Any>>) {
        exerciseplanlist.clear()
        exerciseplanlist.addAll(newData)
        notifyDataSetChanged()
    }

    override fun getItemCount() = exerciseplanlist.size
}