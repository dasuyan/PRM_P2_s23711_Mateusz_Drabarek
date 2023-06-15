package pl.edu.pja.prm_p1_s23711_mateusz_drabarek.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.Geofencing
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.R
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.databinding.FragmentMapsBinding
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.repository.LocationRepository
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.repository.SharedPrefsLocationRepository

const val RANGE = 200.0
private const val STROKE_WIDTH = 10f

class MapsFragment : Fragment() {

    private lateinit var binding: FragmentMapsBinding
    private lateinit var map: GoogleMap
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        locationRepository.savedLocation?.let {
            drawCircle(it)
        }
        turnOnLocation()
        googleMap.setOnMapClickListener(::onMapClick)
    }

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var locationRepository: LocationRepository
    private var current: LatLng? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    @SuppressLint("MissingPermission")
    private val onPermissionResult = { results: Map<String, Boolean> ->
        if (
            ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
            onPermissionResult
        )
        locationRepository = SharedPrefsLocationRepository(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false).also {
            binding = FragmentMapsBinding.bind(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        binding.btBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.btSetCurrent.setOnClickListener {
            // Check if location permission is granted
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Request location updates
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        // Location retrieved successfully
                        current = LatLng(location.latitude, location.longitude)
                        drawCircle(current!!)
                        askForSave(current!!)
                    } ?: run {
                        // Location is null, handle the case when location is unavailable
                        // Display a message or take appropriate action
                    }
                }
            } else {
                // Location permission is not granted, request permission
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    private fun turnOnLocation() {
        if (
            ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun onMapClick(latLng: LatLng) {
        drawCircle(latLng)
        askForSave(latLng)
    }

    private fun askForSave(latLng: LatLng) {
        Snackbar.make(
            requireView(),
            "Save location?",
            Snackbar.LENGTH_INDEFINITE
        ).setAction("Save") {
            save(latLng)
        }.show()
    }

    private fun save(latLng: LatLng) {
        locationRepository.savedLocation = latLng

        (parentFragmentManager.findFragmentByTag(EditFragment::class.java.name) as? EditFragment)?.setPlaceName(requireContext(), latLng)
        Geofencing.createGeofence(requireContext(), latLng)
    }

    private fun drawCircle(latLng: LatLng) {
        val circle = CircleOptions()
            .strokeColor(Color.RED)
            .radius(RANGE)
            .center(latLng)
            .strokeWidth(STROKE_WIDTH)
        map.apply {
            clear()
            addCircle(circle)
        }
    }
}