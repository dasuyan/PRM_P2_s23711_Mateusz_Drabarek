package pl.edu.pja.prm_p1_s23711_mateusz_drabarek.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.Navigable
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.databinding.FragmentPreviewBinding
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.model.Product
// DISABLED FOR NOW
/*
class PreviewFragment(private val product: Product?): Fragment() {
    private lateinit var binding: FragmentPreviewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentPreviewBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.name.text = product?.name
        binding.description.text = product?.description
        product?.let { binding.image.setImageURI(it.resId) }

        binding.btEdit.setOnClickListener {
            (activity as? Navigable)?.navigate(Navigable.Destination.Add, product)
        }
    }
}*/
