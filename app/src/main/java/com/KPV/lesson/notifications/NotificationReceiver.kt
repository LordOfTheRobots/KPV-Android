package com.kpv.lesson.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.RemoteInput

class NotificationReceiver : BroadcastReceiver() {
    companion object {
        const val KEY_REPLY = "key_reply"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val remoteInput = RemoteInput.getResultsFromIntent(intent)
        val repliedText = remoteInput?.getCharSequence(KEY_REPLY)?.toString()

        repliedText?.let { text ->
            Toast.makeText(context, "Ответ получен: $text", Toast.LENGTH_SHORT).show()

            val notificationId = intent.getIntExtra("notification_id", 0)
            NotificationHelper(context).cancelNotification(notificationId)
        }
    }
}