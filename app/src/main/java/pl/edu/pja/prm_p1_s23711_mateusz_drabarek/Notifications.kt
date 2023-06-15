package pl.edu.pja.prm_p1_s23711_mateusz_drabarek

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.service.AlertService

private const val REQUEST_CODE = 10
private const val CHANNEL_ID = "ID_CHANNEL_DEFAULT"

object Notifications {
    fun createChannel(context: Context) {
        val channel = NotificationChannel(CHANNEL_ID, "Wishlist", NotificationManager.IMPORTANCE_HIGH)
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    fun createNotification(context: Context) = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Alarm!")
        .addAction(0, "check again", createIntent(context))
        .build()

    private fun createIntent(context: Context): PendingIntent =
        PendingIntent.getForegroundService(
            context, REQUEST_CODE,
            Intent(context, AlertService::class.java)
                .putExtra("check", true),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
}