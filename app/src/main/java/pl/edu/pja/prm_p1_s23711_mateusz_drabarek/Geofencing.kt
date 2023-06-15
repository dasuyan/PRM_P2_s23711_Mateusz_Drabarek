package pl.edu.pja.prm_p1_s23711_mateusz_drabarek

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.fragments.RANGE
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.service.AlertService

private const val REQUEST_CODE = 2

object Geofencing {

    fun createGeofence(context: Context, latLng: LatLng) {
        val geofence = Geofence.Builder()
            .setCircularRegion(latLng.latitude, latLng.longitude, RANGE.toFloat())
            .setRequestId("home")
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .build()

        val request = GeofencingRequest.Builder()
            .addGeofence(geofence)
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .build()
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", context.packageName, null)
            ).let {
                context.startActivity(it)
            }
        } else {
            LocationServices.getGeofencingClient(context)
                .addGeofences(request, makePendingIntentForAlert(context))
                .addOnFailureListener { println(it) }
                .addOnSuccessListener { println("Dodano geofence") }
        }
    }

    private fun makePendingIntentForAlert(context: Context): PendingIntent =
        PendingIntent.getForegroundService(
            context, REQUEST_CODE,
            Intent(context, AlertService::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
}