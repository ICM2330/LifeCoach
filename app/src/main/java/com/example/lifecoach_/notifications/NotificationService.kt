package com.example.lifecoach_.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
class NotificationService : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // When system Boot completed
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            Log.i("SERVICE", "BOOT COMPLETED")
            val updateIntent = Intent(context, HabitsNotificationService::class.java)
            updateIntent.action = "com.example.lifecoach_.notifications.UPDATE_NOTIFICATIONS"
            context.sendBroadcast(updateIntent)
        }
    }

}