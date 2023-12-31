package com.example.cursproject.push

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.cursproject.R
import com.example.cursproject.view.ServicesActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId = "notification_channel"
const val channelName= "com.example.cursproject"
class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = "Message"

    private fun getRemoteView(title: String, message: String): RemoteViews {
        val remoteView = RemoteViews("com.example.cursproject", R.layout.notification)

        remoteView.setTextViewText(R.id.title, title)
        remoteView.setTextViewText(R.id.message, message)

        return remoteView
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")


            remoteMessage.notification?.let {
                Log.d(TAG, "Message Notification Body: ${it.body}")
            }

        }
        if (remoteMessage.notification != null) {
            val title = remoteMessage.notification!!.title!!
            val message = remoteMessage.notification!!.body!!
            generationNotification(title, message)
        }



    }


    private fun generationNotification(title: String, message: String) {
        val url = "https://www.google.com/"


        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this@MyFirebaseMessagingService, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        var builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, channelId)
                .setSmallIcon(R.drawable.notifi)
                .setAutoCancel(true)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)


        builder = builder.setContent(getRemoteView(title, message))

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0, builder.build())

    }
}



