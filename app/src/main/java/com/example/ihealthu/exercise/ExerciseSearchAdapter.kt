package com.example.ihealthu.exercise

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ihealthu.R

class ExerciseSearchAdapter(private val searchResults: List<Map<String, Any>>,
                            private val fragmentManager: FragmentManager
    ) : RecyclerView.Adapter<ExerciseSearchAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val epIDTextView: TextView = itemView.findViewById(R.id.title_searche)
        val epDescTextView: TextView = itemView.findViewById(R.id.desc_searche)
        val epOwner: TextView = itemView.findViewById(R.id.display_ownername)
        val btndetail: TextView = itemView.findViewById(R.id.btn_checkeplan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_searcheplan, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = searchResults[position]
        val epID = currentItem["epID"]?.toString() ?: "N/A"
        val epOwner = currentItem["epOwner"]?.toString() ?: "N/A"

        holder.epIDTextView.text = epID
        holder.epDescTextView.text = currentItem["epDesc"]?.toString() ?: "N/A"
        holder.epOwner.text = epOwner

        holder.btndetail.setOnClickListener {
            val fragment = Exercise_searchplandetailFragment()

            val bundle = Bundle()
            bundle.putString("epID", epID)
            bundle.putString("epOwner", epOwner)
            fragment.arguments = bundle

            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.framelayout_activitymain, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

    }

    override fun getItemCount() = searchResults.size
}