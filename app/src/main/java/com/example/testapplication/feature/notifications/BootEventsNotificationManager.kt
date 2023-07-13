package com.example.testapplication.feature.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.testapplication.R
import com.example.testapplication.domain.repository.FeatureRepository
import kotlinx.coroutines.*
import javax.inject.Inject

class BootEventsNotificationManager @Inject constructor(
    context: Context,
    private val repository: FeatureRepository
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationChannel: NotificationChannel
    private lateinit var builder: NotificationCompat.Builder
    private val channelId = "notifications_channel_id"
    private val notificationId = 123

    init {
        createNotification(context)
    }

    private fun createNotification(context: Context) = scope.launch {
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "channel_name"
            val descriptionText = "channel_description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            notificationChannel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
                enableLights(true)
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(notificationChannel)
            builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_notifications_24)
                .setContentText(getNotificationText())
//                .setStyle(NotificationCompat.MessagingStyle("Me")
//                    .addMessage("1 ", 0L, null))
//                .setContentIntent(pendingIntent)
            Log.d("StuupidBoot", "BootEventsNotificationManager.createNotification >= Build.VERSION_CODES.O text=${getNotificationText()}")
        } else {
            builder = NotificationCompat.Builder(context, "")
                .setSmallIcon(R.drawable.ic_notifications_24)
                .setContentText(getNotificationText())
//                .setContentIntent(pendingIntent)
            Log.d("StuupidBoot", "BootEventsNotificationManager.createNotification text=${getNotificationText()}")
        }
        notificationManager.notify(notificationId, builder.build())
    }

    private suspend fun getNotificationText(): String {
        var textTimestamp = ""
        val timestamps =
        scope.async {
            repository.getBootEventTimestamp()
        }.await()

        Log.d("StuupidBoot", "BootEventsNotificationManager.getNotificationText before timestamps=$timestamps")
        if (timestamps.isNotEmpty()) {
            textTimestamp = if (timestamps.size == 1) {
                Log.d("StuupidBoot", "BootEventsNotificationManager.getNotificationText timestamp[0]=${timestamps[0]}")
                "The boot was detected with the timestamp = ${timestamps[0]}"
            } else if (timestamps.size >= 2){
                val last = timestamps.getOrNull(timestamps.lastIndex)
                val beforeLast = timestamps.getOrNull(timestamps.lastIndex - 1)
                Log.d("StuupidBoot", "BootEventsNotificationManager.getNotificationText timestamps.size > 2 =$timestamps")
                "“Last boots time delta = ${(last?.minus(beforeLast ?: 0L)).toString()}"
            } else {
                "No boots detected"
            }
        }
        Log.d("StuupidBoot", "BootEventsNotificationManager.getNotificationText textTimestamp text=$textTimestamp")
        return textTimestamp
    }

}