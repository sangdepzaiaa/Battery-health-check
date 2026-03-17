package com.example.myapplication.utils

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.ui.home.ResultActivity

object NotificationHelper {

    fun showNotification(context: Context, health: Int) {

        val intent = Intent(context, ResultActivity::class.java)
        intent.putExtra("battery_health", health)

        val pendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )

        val builder =
            NotificationCompat.Builder(context, "battery_channel")
                .setSmallIcon(R.drawable.ic_set_battery)
                .setContentTitle("Battery Health Result")
                .setContentText("Tap to see result")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                NotificationManagerCompat
                    .from(context)
                    .notify(1001, builder.build())
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
    }
}