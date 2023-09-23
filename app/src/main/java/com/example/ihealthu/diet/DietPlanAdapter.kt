package com.example.ihealthu.diet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ihealthu.R

class DietPlanAdapter(private val dietDataList: MutableList<Map<String, Any>>) : RecyclerView.Adapter<DietPlanAdapter.DietViewHolder>() {

    class DietViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bfTime: TextView = itemView.findViewById(R.id.bfTime)
        val bfEsKl: TextView = itemView.findViewById(R.id.bfEsKl)
        val bfRemark: TextView = itemView.findViewById(R.id.bfRemark)
        val luTime: TextView = itemView.findViewById(R.id.luTime)
        val luEsKl: TextView = itemView.findViewById(R.id.luEsKl)
        val luRemark: TextView = itemView.findViewById(R.id.luRemark)
        val dnTime: TextView = itemView.findViewById(R.id.dnTime)
        val dnEsKl: TextView = itemView.findViewById(R.id.dnEsKl)
        val dnRemark: TextView = itemView.findViewById(R.id.dnRemark)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DietViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_diet, parent, false)
        return DietViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DietViewHolder, position: Int) {
        val currentItem = dietDataList[position]

        holder.bfTime.text = currentItem["dpBftime"]?.toString() ?: "N/A"
        holder.bfEsKl.text = currentItem["dpBfkals"]?.toString() ?: "N/A"
        holder.bfRemark.text = currentItem["dpBfRemark"]?.toString() ?: "N/A"
        holder.luTime.text = currentItem["dpLutime"]?.toString() ?: "N/A"
        holder.luEsKl.text = currentItem["dpLukals"]?.toString() ?: "N/A"
        holder.luRemark.text = currentItem["dpLuRemark"]?.toString() ?: "N/A"
        holder.dnTime.text = currentItem["dpDntime"]?.toString() ?: "N/A"
        holder.dnEsKl.text = currentItem["dpDnkals"]?.toString() ?: "N/A"
        holder.dnRemark.text = currentItem["dpDnRemark"]?.toString() ?: "N/A"
    }
    fun submitList(newData: List<Map<String, Any>>) {
        dietDataList.clear()
        dietDataList.addAll(newData)
        notifyDataSetChanged()
    }

    override fun getItemCount() = dietDataList.size
}
