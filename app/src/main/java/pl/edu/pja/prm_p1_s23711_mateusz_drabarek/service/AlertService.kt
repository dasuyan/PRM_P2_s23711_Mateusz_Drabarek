package pl.edu.pja.prm_p1_s23711_mateusz_drabarek.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.location.Location
import android.media.MediaPlayer
import android.os.IBinder
import android.os.Looper
import com.google.android.gms.location.*
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.Notifications
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.R
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.fragments.RANGE
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.repository.LocationRepository
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.repository.SharedPrefsLocationRepository

private const val NOTIFICATION_ID = 1

class AlertService : Service() {

    private lateinit var mp: MediaPlayer
    private lateinit var locationRepository: LocationRepository
    private lateinit var locationClient : FusedLocationProviderClient

    private val callback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(loc: LocationResult) {
            locationRepository.savedLocation?.let { saved ->
                val current = loc.lastLocation
                val result = FloatArray(1)
                if (current != null) {
                    Location.distanceBetween(
                        current.latitude, current.longitude,
                        saved.latitude, saved.longitude,
                        result
                    )
                }
                if (result[0] <= RANGE) {
                    mp.stop()
                    stopSelf()
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        mp = MediaPlayer().apply {
            setDataSource(resources.openRawResourceFd(R.raw.alarm))
        }
        locationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRepository = SharedPrefsLocationRepository(this)
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.getBooleanExtra("check", false) == true) {
            checkLocation()
        } else {
            val geofence = intent?.let { GeofencingEvent.fromIntent(it) }
            val id = geofence?.triggeringGeofences?.firstOrNull()?.requestId
            if (id == "home") {
                mp.apply {
                    setOnPreparedListener { mp.start() }
                    isLooping = true
                    prepareAsync()
                }
            }
        }
        startForeground(NOTIFICATION_ID, Notifications.createNotification(this))
        return START_STICKY
    }

    @SuppressLint("MissingPermission")
    private fun checkLocation() {
        val req = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 100
            numUpdates = 1
        }
        locationClient.requestLocationUpdates(req, callback, Looper.getMainLooper())
    }

    override fun onDestroy() {
        mp.apply {
            stop()
            release()
        }
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? = null
}