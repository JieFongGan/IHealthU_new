package com.example.ihealthu.exercise

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ihealthu.R

class ExerciseSearchAdapter(private val searchResults: List<Map<String, Any>>) : RecyclerView.Adapter<ExerciseSearchAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val epIDTextView: TextView = itemView.findViewById(R.id.title_searche)
        val epDescTextView: TextView = itemView.findViewById(R.id.desc_searche)
        val epOwner: TextView = itemView.findViewById(R.id.display_ownername)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_searcheplan, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = searchResults[position]

        holder.epIDTextView.text = currentItem["epID"]?.toString() ?: "N/A"
        holder.epDescTextView.text = currentItem["epDesc"]?.toString() ?: "N/A"
        holder.epOwner.text = currentItem["epOwner"]?.toString() ?: "N/A"
    }

    override fun getItemCount() = searchResults.size
}