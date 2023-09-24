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
    private val onDeleteClickListener: (position: Int) -> Unit
) : RecyclerView.Adapter<MyeplanAdapter.MyViewHolder>() {

    private lateinit var epID: String

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val epIDTextView: TextView = itemView.findViewById(R.id.title_searche)
        val epDescTextView: TextView = itemView.findViewById(R.id.desc_searche)
        val deleteeplan: Button = itemView.findViewById(R.id.btn_deleteeplan)
        val editeplan: Button = itemView.findViewById(R.id.btn_editeplan)
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

        holder.epIDTextView.text = currentItem["epID"]?.toString() ?: "N/A"
        holder.epDescTextView.text = currentItem["epDesc"]?.toString() ?: "N/A"

        epID = currentItem["epID"]?.toString() ?: "N/A"
        holder.deleteeplan.setOnClickListener {
            showDeleteConfirmationDialog(position, epID)
        }

        holder.editeplan.setOnClickListener {
            val fragment = Exercise_MpeditFragment()

            val bundle = Bundle()
            bundle.putString("epID", epID)
            fragment.arguments = bundle

            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.framelayout_activitymain, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
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
            // User clicked No, dismiss the dialog
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
