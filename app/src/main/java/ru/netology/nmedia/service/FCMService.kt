package ru.netology.nmedia.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import ru.netology.nmedia.R
import kotlin.random.Random
import java.lang.IllegalArgumentException

class FCMService:FirebaseMessagingService() {
    private val action = "action"
    private val content = "content"
    private val channelId = "customChannel"
    private val gson = Gson()

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_remote_name)
            val descriptionText = getString(R.string.channel_remote_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        message.data[action]?.let {
            try {
                when (Action.valueOf(it)) {
                    Action.LIKE -> handleLike(
                        gson.fromJson(
                            message.data[content],
                            Like::class.java
                        )
                    )
                    Action.NEW_POST -> handleNewPost(
                        gson.fromJson(
                            message.data[content],
                            NewPost::class.java
                        )
                    )
                }
            }
            catch (exp : IllegalArgumentException){
                handleNotImplemented()
            }
        println(Gson().toJson(message))
        }
    }

    override fun onNewToken(token: String) {
        println(token)
    }

    private fun handleNewPost(content: NewPost) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_new_post,
                    content.userName
                )
            )
            //.setContentText(content.postText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content.postText))
            .build()

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100_000),notification)
    }

    private fun handleLike(content: Like) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_user_liked,
                    content.userName,
                    content.postAuthor,
                )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100_000),notification)
    }

    private fun handleNotImplemented() {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_system_not_supported,
                )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentText(getString(R.string.notification_upgrade_recommended))
            .build()

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100_000),notification)
    }

    enum class Action {
        LIKE, NEW_POST,
    }

    class Like(
        val userId: Long,
        val userName: String,
        val postId: Long,
        val postAuthor: String
    )

    class NewPost(
        val userId: Long,
        val userName: String,
        val postId: Long,
        val postAuthor: String,
        val postText: String
    )
}