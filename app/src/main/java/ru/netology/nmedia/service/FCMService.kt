package ru.netology.nmedia.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson

class FCMService:FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        //super.onMessageReceived(message)
        println(Gson().toJson(message))
    }

    override fun onNewToken(token: String) {
        //super.onNewToken(token)
        println(token)
    }
}