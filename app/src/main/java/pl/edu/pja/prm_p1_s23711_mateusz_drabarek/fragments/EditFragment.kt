package pl.edu.pja.prm_p1_s23711_mateusz_drabarek.fragments

import android.content.Context
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.Navigable
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.R
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.data.ProductDatabase
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.data.model.ProductEntity
//import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.data.DataSource
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.databinding.FragmentEditBinding
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.model.Product
import java.util.Locale


class EditFragment(private val product: Product?) : Fragment(){

    private lateinit var binding: FragmentEditBinding
    private lateinit var db: ProductDatabase
    private var taskDb: ProductEntity? = null
    private var imageUri: Uri? = null

    companion object {
        var locationText: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = ProductDatabase.open(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentEditBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (product == null) {
            handleNewTask()
        } else {
            handleEditTask()
        }

    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun handleNewTask() {
        binding.editFragmentTitle.setText(R.string.edit_fragment_adding)

        binding.btLocate.setOnClickListener {
            (activity as? Navigable)?.navigate(Navigable.Destination.Map, null)
        }

        binding.takePhoto.setOnClickListener {
            (activity as? Navigable)?.navigate(Navigable.Destination.Photo, null)
        }

        binding.btSave.setOnClickListener {
            val newTask = ProductEntity(
                name = binding.taskName.text.toString(),
                description = locationText,
                icon = imageUri.toString()
            )
            this.taskDb = newTask

            GlobalScope.launch {
                db.tasks.addTask(taskDb!!)
                (activity as? Navigable)?.navigate(Navigable.Destination.List, null)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun handleEditTask() {
        binding.editFragmentTitle.setText(R.string.edit_fragment_editing)

        GlobalScope.launch {
            taskDb = product?.let { db.tasks.getTask(it.id) }
        }

        binding.taskName.setText(product?.name)
        binding.description.setText(product?.description)

        binding.btLocate.setOnClickListener {
            (activity as? Navigable)?.navigate(Navigable.Destination.Map, null)
        }

        binding.takePhoto.setOnClickListener {
            (activity as? Navigable)?.navigate(Navigable.Destination.Photo, null)
        }

        binding.imageView.setImageURI(product?.resId)

        binding.btSave.setOnClickListener {
            if (imageUri == null) {
                imageUri = taskDb?.icon?.toUri()
            }
            val editTask = ProductEntity(
                id = taskDb?.id ?: 0,
                name = binding.taskName.text.toString(),
                description = locationText,
                icon = imageUri.toString()
            )
            this.taskDb = editTask

            GlobalScope.launch {
                db.tasks.addTask(taskDb!!)
                (activity as? Navigable)?.navigate(Navigable.Destination.List, null)
            }
        }
    }

    fun setPlaceName(context: Context, latLng: LatLng) {
        val geocoder = Geocoder(context, Locale.getDefault())
        val geocodeListener = Geocoder.GeocodeListener { addresses ->
            locationText = addresses[0].getAddressLine(0)
        }
        geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1, geocodeListener)
        binding.description.setText(locationText)
    }

    fun setPhoto(uri: Uri) {
        imageUri = uri
        binding.imageView.setImageURI(imageUri)
    }
    override fun onResume() {
        super.onResume()
        if (binding.imageView.drawable == null) {
            binding.imageView.setImageURI(imageUri)
        }
        if (binding.description.text.toString() == ""
            || (binding.description.text.toString() != locationText) && locationText != "") {
            binding.description.setText(locationText)
        }
    }
    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }
}