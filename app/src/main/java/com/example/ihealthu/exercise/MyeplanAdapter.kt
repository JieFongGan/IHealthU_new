import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ihealthu.R
import com.example.ihealthu.exercise.Exercise_MpeditFragment

class MyeplanAdapter(
    private val context: Context,
    private val fragmentManager: FragmentManager,
    private val MyeplanDataList: MutableList<Map<String, Any>>,
    private val onDeleteClickListener: (position: Int) -> Unit,
    private val onStatusUpdateClickListener: (epID: String) -> Unit
) : RecyclerView.Adapter<MyeplanAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val epIDTextView: TextView = itemView.findViewById(R.id.title_searche)
        val epDescTextView: TextView = itemView.findViewById(R.id.desc_searche)
        val statusTextView: TextView = itemView.findViewById(R.id.status_myplan)
        val deleteeplan: Button = itemView.findViewById(R.id.btn_deleteeplan)
        val editeplan: Button = itemView.findViewById(R.id.btn_editeplan)
        val btnNewEplan: Button = itemView.findViewById(R.id.btn_activeeplan)
    }

    fun getItemAtPosition(position: Int): Map<String, Any> {
        return MyeplanDataList[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyeplanAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_myeplan, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = MyeplanDataList[position]

        val epID = currentItem["epID"]?.toString() ?: "N/A"
        val epOwner = currentItem["epOwner"]?.toString() ?: "N/A"

        holder.epIDTextView.text = epID
        holder.epDescTextView.text = currentItem["epDesc"]?.toString() ?: "N/A"
        holder.statusTextView.text = currentItem["status"]?.toString() ?: "N/A"

        holder.deleteeplan.setOnClickListener {
            showDeleteConfirmationDialog(position, epID)
        }

        holder.editeplan.setOnClickListener {
            val fragment = Exercise_MpeditFragment()

            val bundle = Bundle()
            bundle.putString("epID", epID)
            bundle.putString("epOwner", epOwner)
            fragment.arguments = bundle

            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.framelayout_activitymain, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        holder.btnNewEplan.setOnClickListener {
            val clickedItem = getItemAtPosition(position)
            val epID = clickedItem["epID"].toString()

            onStatusUpdateClickListener(epID)
        }
    }

    private fun showDeleteConfirmationDialog(position: Int, epID: String) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Delete Confirmation")
        alertDialogBuilder.setMessage("Are you sure you want to delete this item?")
        alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
            onDeleteClickListener.invoke(position)
        }
        alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    fun submitList(newData: List<Map<String, Any>>) {
        MyeplanDataList.clear()
        MyeplanDataList.addAll(newData)
        notifyDataSetChanged()
    }

    override fun getItemCount() = MyeplanDataList.size
}
