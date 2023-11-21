package com.example.lifecoach_.notifications

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import com.example.lifecoach_.R
import com.example.lifecoach_.activities.MainActivity
import com.example.lifecoach_.activities.habits.view.GenericHabitViewActivity
import com.example.lifecoach_.model.habits.Habit

class HabitNotification : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val habit = intent.getSerializableExtra("habit") as Habit
        raiseNotification(context, habit, MainActivity::class.java)
        Log.i("NOTI", "Notificacion creada")
    }

    private fun raiseNotification(context: Context, habit: Habit, target: Class<*>) {
        val notification: Notification = buildNotification(
            context,
            "Es hora de realizar tu habito!",
            habit.name,
            R.drawable.headerlogolifecoach,
            habit,
            target
        )

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Check if has notification permissions
        if (checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        )
            manager.notify(NotiId, notification)

        NotiId++
    }

    private fun buildNotification(
        context: Context,
        title: String,
        message: String,
        icon: Int,
        habit: Habit,
        target: Class<*>
    ): Notification {
        // Build the notification
        val builder = NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setSmallIcon(icon)
            setContentTitle(title)
            setContentText(message)
            priority = NotificationCompat.PRIORITY_DEFAULT
            setAutoCancel(true)
        }

        // Start the activity from the notification with a task stack
        val resultIntent = Intent(context, target).apply {
            putExtra("habit", habit)
            //flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASKT
        }
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(
                NotiId,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
        builder.setContentIntent(resultPendingIntent)

        // Return the built notification
        return builder.build()
    }

    companion object {
        public var NotiId = 0
        const val CHANNEL_ID = "Habits"
    }
}