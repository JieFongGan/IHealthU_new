package com.example.ihealthu.diet

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ihealthu.R
import com.example.ihealthu.database.DietPlan

class DogRecyclerViewAdapter : ListAdapter<DietPlan, DogRecyclerViewAdapter.ViewHolder>(DietDiffCallback()) {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val bfTime: TextView = itemView.findViewById(R.id.bfTime)
        private val bfRatio: TextView = itemView.findViewById(R.id.bfRatio)
        private val bfEsKl: TextView = itemView.findViewById(R.id.bfEsKl)
        private val bfRemark: TextView = itemView.findViewById(R.id.bfRemark)
        private val luTime: TextView = itemView.findViewById(R.id.luTime)
        private val luRatio: TextView = itemView.findViewById(R.id.luRatio)
        private val luEsKl: TextView = itemView.findViewById(R.id.luEsKl)
        private val luRemark: TextView = itemView.findViewById(R.id.luRemark)
        private val dnTime: TextView = itemView.findViewById(R.id.dnTime)
        private val dnRatio: TextView = itemView.findViewById(R.id.dnRatio)
        private val dnEsKl: TextView = itemView.findViewById(R.id.dnEsKl)
        private val dnRemark: TextView = itemView.findViewById(R.id.dnRemark)
        fun bind(dietPlan: DietPlan) {
            bfTime.text = dietPlan.dpBftime
            bfRatio.text = dietPlan.dpBfRatio
            bfEsKl.text = dietPlan.dpBfEsKl
            bfRemark.text = dietPlan.dpBfRemark
            luTime.text = dietPlan.dpLutime
            luRatio.text = dietPlan.dpLuRatio
            luEsKl.text = dietPlan.dpLuEsKl
            luRemark.text = dietPlan.dpLuRemark
            dnTime.text = dietPlan.dpDntime
            dnRatio.text = dietPlan.dpDnRatio
            dnEsKl.text = dietPlan.dpDnEsKl
            dnRemark.text = dietPlan.dpDnRemark
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_item_diet, parent, false)
        Log.i("Testing", "qweqweqweqwe")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dietPlan = getItem(position)
        holder.bind(dietPlan)
    }

    class DietDiffCallback : DiffUtil.ItemCallback<DietPlan>() {
        override fun areItemsTheSame(oldItem: DietPlan, newItem: DietPlan): Boolean {
            return oldItem.dpID == newItem.dpID
        }

        override fun areContentsTheSame(oldItem: DietPlan, newItem: DietPlan): Boolean {
            return oldItem == newItem
        }
    }
}

