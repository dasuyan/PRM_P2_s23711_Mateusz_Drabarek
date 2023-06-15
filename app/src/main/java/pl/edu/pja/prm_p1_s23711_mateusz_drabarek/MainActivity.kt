package pl.edu.pja.prm_p1_s23711_mateusz_drabarek

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.fragments.EditFragment
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.fragments.ListFragment
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.fragments.MapsFragment
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.fragments.PaintFragment
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.fragments.SettingsFragment
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.model.Product

class MainActivity : AppCompatActivity(), Navigable {
    private lateinit var listFragment: ListFragment
    private lateinit var settingsFragment: SettingsFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Notifications.createChannel(this)

        listFragment = ListFragment()

        supportFragmentManager.beginTransaction()
            .add(R.id.container, listFragment, listFragment.javaClass.name)
            .commit()
    }

    override fun navigate(to: Navigable.Destination, product: Product?) {
        supportFragmentManager.beginTransaction().apply {
            when (to) {
                Navigable.Destination.List -> {
                    replace(R.id.container, listFragment, listFragment.javaClass.name)
                }
                Navigable.Destination.Add -> {
                    replace(R.id.container, EditFragment(product), EditFragment::class.java.name)
                    addToBackStack(EditFragment::class.java.name)
                }
                Navigable.Destination.Photo -> {
                    replace(R.id.container, PaintFragment(), PaintFragment::class.java.name)
                    addToBackStack(PaintFragment::class.java.name)
                }
                Navigable.Destination.Map -> {
                    replace(R.id.container, MapsFragment(), MapsFragment::class.java.name)
                    addToBackStack(MapsFragment::class.java.name)
                }
            }
        }.commit()
    }

    fun addSettingsFragment() {
        supportFragmentManager.beginTransaction().apply {
            settingsFragment = SettingsFragment()
            add(R.id.container, settingsFragment, settingsFragment.javaClass.name)
        }.commit()
    }

    fun removeSettingsFragment() {
        supportFragmentManager.beginTransaction().apply {
            remove(settingsFragment)
        }.commit()
    }
}