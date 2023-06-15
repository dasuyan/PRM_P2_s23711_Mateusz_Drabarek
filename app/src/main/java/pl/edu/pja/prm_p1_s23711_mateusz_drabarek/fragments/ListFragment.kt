package pl.edu.pja.prm_p1_s23711_mateusz_drabarek.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.Navigable
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.adapters.TasksAdapter
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.data.ProductDatabase
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.databinding.FragmentListBinding
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.model.Product

class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private var adapter: TasksAdapter? = null
    lateinit var db: ProductDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = ProductDatabase.open(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentListBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navigable = activity as? Navigable
        adapter = navigable?.let { TasksAdapter(this, it) }
        loadData()

        binding.list.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }

        binding.btAdd.setOnClickListener {
            (activity as? Navigable)?.navigate(Navigable.Destination.Add, null)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun loadData() {
        GlobalScope.launch {
            val products = db.tasks.getAll().map { entity ->
                Product(
                    entity.id,
                    entity.name,
                    entity.description,
                    entity.icon.toUri()
                )
            }
            binding.allTasksValue.text = products.size.toString()
            adapter?.replace(products)
        }
    }
    override fun onStart() {
        super.onStart()
        loadData()
    }
    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }
}