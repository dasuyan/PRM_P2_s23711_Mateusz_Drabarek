package pl.edu.pja.prm_p1_s23711_mateusz_drabarek.fragments

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.databinding.FragmentSettingsBinding
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.model.Settings


class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentSettingsBinding.inflate(
        inflater, container, false
    ).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.photoNote.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                changeSettings()
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.btSave.setOnClickListener {
            (parentFragmentManager.findFragmentByTag(PaintFragment::class.java.name) as? PaintFragment)?.savePhoto()
            parentFragmentManager.popBackStack()
        }
    }

    private fun changeSettings() {
        val settings = Settings(
            Color.YELLOW,
            10f,
            binding.photoNote.text.toString()
        )
        (parentFragmentManager.findFragmentByTag(PaintFragment::class.java.name) as? PaintFragment)?.setSettings(settings)
    }
}