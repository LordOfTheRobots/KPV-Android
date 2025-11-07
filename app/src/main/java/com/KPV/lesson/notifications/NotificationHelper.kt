package com.kpv.lesson.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import com.kpv.lesson.MainActivity
import com.kpv.lesson.notifications.NotificationReceiver.Companion.KEY_REPLY
import kotlin.jvm.java

class NotificationHelper(private val context: Context) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        const val CHANNEL_HIGH = "high_importance"
        const val CHANNEL_DEFAULT = "default_importance"
        const val CHANNEL_LOW = "low_importance"
        const val CHANNEL_MIN = "min_importance"
    }

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val highChannel = NotificationChannel(
                CHANNEL_HIGH,
                "Высокий приоритет",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Уведомления высокого приоритета"
            }

            val defaultChannel = NotificationChannel(
                CHANNEL_DEFAULT,
                "Средний приоритет",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Уведомления среднего приоритета"
            }

            val lowChannel = NotificationChannel(
                CHANNEL_LOW,
                "Низкий приоритет",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Уведомления низкого приоритета"
            }

            val minChannel = NotificationChannel(
                CHANNEL_MIN,
                "Минимальный приоритет",
                NotificationManager.IMPORTANCE_MIN
            ).apply {
                description = "Уведомления минимального приоритета"
            }

            notificationManager.createNotificationChannels(
                listOf(highChannel, defaultChannel, lowChannel, minChannel)
            )
        }
    }

    fun sendNotification(
        id: Int,
        title: String,
        text: String?,
        priority: Importance,
        expandable: Boolean = false,
        openApp: Boolean = false,
        withReply: Boolean = false
    ) {
        val channelId = when (priority) {
            Importance.HIGH -> CHANNEL_HIGH
            Importance.MEDIUM -> CHANNEL_DEFAULT
            Importance.LOW -> CHANNEL_LOW
            Importance.MIN -> CHANNEL_MIN
        }

        val contentIntent = if (openApp) {
            val intent = Intent(context, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                putExtra("notification_title", title)
                putExtra("notification_text", text)
            }
            PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            null
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setPriority(getNotificationPriority(priority))

        if (!text.isNullOrEmpty()) {
            builder.setContentText(text)
            if (expandable && text.length > 50) {
                builder.setStyle(NotificationCompat.BigTextStyle().bigText(text))
            }
        }

        contentIntent?.let { builder.setContentIntent(it) }

        if (withReply) {
            val remoteInput = RemoteInput.Builder(KEY_REPLY)
                .setLabel("Введите ответ")
                .build()
            val replyIntent = Intent(context, NotificationReceiver::class.java).apply {
                putExtra("notification_id", id)
            }
            val replyPendingIntent = PendingIntent.getBroadcast(
                context,
                id,
                replyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            val action = NotificationCompat.Action.Builder(
                android.R.drawable.ic_menu_send,
                "Ответить",
                replyPendingIntent
            ).addRemoteInput(remoteInput).build()

            builder.addAction(action)
        }

        notificationManager.notify(id, builder.build())
    }

    fun updateNotification(id: Int, newText: String): Boolean {
        val activeNotifications = notificationManager.activeNotifications
        val notificationExists = activeNotifications.any { it.id == id }

        if (notificationExists) {
            val builder = NotificationCompat.Builder(context, CHANNEL_DEFAULT)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Обновленное уведомление")
                .setContentText(newText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            notificationManager.notify(id, builder.build())
            return true
        }
        return false
    }

    fun cancelNotification(id: Int) {
        notificationManager.cancel(id)
    }

    fun cancelAllNotifications() {
        notificationManager.cancelAll()
    }

    fun hasActiveNotifications(): Boolean {
        return notificationManager.activeNotifications.isNotEmpty()
    }

    private fun getNotificationPriority(importance: Importance): Int {
        return when (importance) {
            Importance.HIGH -> NotificationCompat.PRIORITY_HIGH
            Importance.MEDIUM -> NotificationCompat.PRIORITY_DEFAULT
            Importance.LOW -> NotificationCompat.PRIORITY_LOW
            Importance.MIN -> NotificationCompat.PRIORITY_MIN
        }
    }
}

enum class Importance {
    HIGH, MEDIUM, LOW, MIN
}