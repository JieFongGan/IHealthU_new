package com.example.ihealthu.diet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ihealthu.R

class DietSearchAdapter(var dietDataList: MutableList<Map<String, Any>>) : RecyclerView.Adapter<DietSearchAdapter.DietViewHolder>() {
    class DietViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dsOwnerEmail: TextView = itemView.findViewById(R.id.dsOwnerEmail)
        val dsPlanDays: TextView = itemView.findViewById(R.id.dsPlanDays)
        val dsPlanDesc: TextView = itemView.findViewById(R.id.dsPlanDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DietViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_diet_search, parent, false)
        return DietViewHolder(view)
    }

    override fun onBindViewHolder(holder: DietViewHolder, position: Int) {
        val data = dietDataList[position]
        holder.dsOwnerEmail.text = data["dpOwnerName"] as String? ?: "N/A"
        holder.dsPlanDays.text = data["dpDietDays"] as String? ?: "N/A"
        holder.dsPlanDesc.text = data["dpPlanPP"] as String? ?: "N/A"
    }

    override fun getItemCount(): Int {
        return dietDataList.size
    }
}
