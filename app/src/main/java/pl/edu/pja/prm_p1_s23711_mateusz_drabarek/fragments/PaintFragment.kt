package pl.edu.pja.prm_p1_s23711_mateusz_drabarek.fragments

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.MainActivity
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.databinding.FragmentPaintBinding
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.model.Settings

class PaintFragment : Fragment() {

    private lateinit var binding: FragmentPaintBinding
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private var imageUri: Uri? = null

    private val onTakePhoto: (Boolean) -> Unit = { photo: Boolean ->
        if (photo) {
            loadBitmap()
            } else {
                imageUri?.let {
                    requireContext().contentResolver
                        .delete(it, null, null)
                }
        }
    }

    private fun loadBitmap() {
        val imageUri = imageUri ?: return
        requireContext().contentResolver
            .openInputStream(imageUri)
            ?.use {
                BitmapFactory.decodeStream(it)
            }?.let {
                binding.paintView.background = it
                (activity as MainActivity).addSettingsFragment()
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraLauncher = registerForActivityResult(
            ActivityResultContracts.TakePicture(),
            onTakePhoto
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentPaintBinding.inflate(
        inflater, container, false
    ).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        createPicture()
    }

    private fun createPicture() {
        val imagesUri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

        val ct = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "photo.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        }
        imageUri = requireContext().contentResolver.insert(imagesUri, ct)
        cameraLauncher.launch(imageUri)
    }

    fun setSettings(settings: Settings) {
        binding.paintView.apply {
            color = settings.color
            size = settings.size
            text = settings.text
            invalidate()
        }
    }

    fun savePhoto() {
        val imageUri = imageUri ?: return
        val bmp = binding.paintView.generateBitmap()
        (parentFragmentManager.findFragmentByTag(EditFragment::class.java.name) as? EditFragment)?.setPhoto(imageUri)
        requireContext().contentResolver.openOutputStream(imageUri)?.use {
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }

    }

    override fun onDestroy() {
        (activity as MainActivity).removeSettingsFragment()
        super.onDestroy()
    }
}