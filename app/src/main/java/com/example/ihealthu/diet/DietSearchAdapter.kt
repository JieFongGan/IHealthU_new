package com.example.ihealthu.diet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.findNavController
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ihealthu.R

class DietSearchAdapter(var dietDataList: MutableList<Map<String, Any>>,
                        private val fragmentManager: FragmentManager) : RecyclerView.Adapter<DietSearchAdapter.DietViewHolder>() {
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
        //item onclick *the card view
        holder.itemView.setOnClickListener {
            val selectedOwnerEmail = data["dpOwnerName"] as String? ?: "N/A"
            val selectedPlanDays = data["dpDietDays"] as String? ?: "N/A"

            val detailFragment = DietSearchDetailFragment()
            //cokkie
            val bundle= Bundle()
            bundle.putString("ownerEmail", selectedOwnerEmail)
            bundle.putString("planDays", selectedPlanDays)
            detailFragment.arguments = bundle

            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.framelayout_activitymain, detailFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }



    override fun getItemCount(): Int {
        return dietDataList.size
    }
}
