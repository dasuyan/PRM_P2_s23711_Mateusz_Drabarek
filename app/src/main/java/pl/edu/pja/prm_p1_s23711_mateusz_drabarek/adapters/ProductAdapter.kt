package pl.edu.pja.prm_p1_s23711_mateusz_drabarek.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.HandlerCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.Navigable
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.ProductCallback
//import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.data.DataSource
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.databinding.ListItemBinding
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.fragments.ListFragment
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.model.Product


class TaskViewHolder(private val binding: ListItemBinding)
    : RecyclerView.ViewHolder(binding.root) {
    fun bind(product: Product) {
        binding.name.text = product.name
        binding.description.text = product.description
        binding.image.setImageURI(product.resId)
    }
}

class TasksAdapter(private val listFragment: ListFragment, private val navigable: Navigable) : RecyclerView.Adapter<TaskViewHolder>() {
    private val handler: Handler = HandlerCompat.createAsync(Looper.getMainLooper())
    private val data = mutableListOf<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return TaskViewHolder(binding)
    }

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(data[position])

        holder.itemView.setOnClickListener{
            navigable.navigate(Navigable.Destination.Add, data[position])
        }

        holder.itemView.setOnLongClickListener{
            val context = it.context
            val builder = AlertDialog.Builder(context)

            builder.setTitle("Remove task")
            builder.setMessage("Do you want to remove this task?")
            builder.setPositiveButton("Yes") { dialog, _ ->
                GlobalScope.launch {
                    val database = listFragment.db
                    database.tasks.removeTask(data[position].id)
                    listFragment.loadData()
                }
                notifyDataSetChanged()

                dialog.dismiss()
                val text = "Task successfully removed"
                val duration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(context, text, duration)
                toast.show()
            }
            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
            true
        }
    }

    override fun getItemCount(): Int = data.size

    fun replace(newData: List<Product>) {
        val callback = ProductCallback(data, newData)
        data.clear()
        data.addAll(newData)
        val result = DiffUtil.calculateDiff(callback)
        handler.post{
            result.dispatchUpdatesTo(this)
        }
    }
}