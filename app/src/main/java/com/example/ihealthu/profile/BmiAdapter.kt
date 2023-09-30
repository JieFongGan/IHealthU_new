package com.example.ihealthu.profile

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.ihealthu.R

class BmiAdapter(
    private val context: Context,
    private val bmiRecords: MutableList<Map<String, Any>>
) : RecyclerView.Adapter<BmiAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Declare views here
        val timestampTextView: TextView = itemView.findViewById(R.id.timeRecorded)
        val bmiTextView: TextView = itemView.findViewById(R.id.bmiRecorded)
        val healthyMessageTextView: TextView = itemView.findViewById(R.id.healthyMessageRecorded)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_added_bmi, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = bmiRecords[position]

        holder.timestampTextView.text = record["timestamp"]?.toString() ?: "N/A"
        holder.bmiTextView.text = record["bmi"]?.toString() ?: "N/A"
        holder.healthyMessageTextView.text = record["healthyMessage"]?.toString() ?: "N/A"

    }

    fun submitList(newData: List<Map<String, Any>>) {
        bmiRecords.clear()
        bmiRecords.addAll(newData)
        notifyDataSetChanged()
    }

    override fun getItemCount() = bmiRecords.size
}
