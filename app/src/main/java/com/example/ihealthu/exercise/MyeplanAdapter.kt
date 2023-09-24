import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ihealthu.R

class MyeplanAdapter(private val MyeplanDataList: MutableList<Map<String, Any>>,
                     private val onDeleteClickListener: (position: Int) -> Unit
) : RecyclerView.Adapter<MyeplanAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val epIDTextView: TextView = itemView.findViewById(R.id.title_searche)
        val epDescTextView: TextView = itemView.findViewById(R.id.desc_searche)
        val deleteeplan: Button = itemView.findViewById(R.id.btn_deleteeplan)
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

        // Handle delete button click
        holder.deleteeplan.setOnClickListener {
            onDeleteClickListener.invoke(position)
        }
    }

    fun submitList(newData: List<Map<String, Any>>) {
        MyeplanDataList.clear()
        MyeplanDataList.addAll(newData)
        notifyDataSetChanged()
    }

    override fun getItemCount() = MyeplanDataList.size
}
