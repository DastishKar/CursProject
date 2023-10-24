package com.example.cursproject.push

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseCloudMessaging: FirebaseMessagingService() {
    override fun onNewToken(token: String) {


    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {

    }
}