package com.example.lifecoach_.notifications

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.SystemClock
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.lifecoach_.model.habits.Habit
import com.example.lifecoach_.services.firebase.HabitsService
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class HabitsNotificationService : BroadcastReceiver() {
    private val habitsService = HabitsService()
    private val auth = Firebase.auth

    override fun onReceive(context: Context, intent: Intent) {
        Log.i("NOTI", "Request for update notifications received")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        auth.currentUser?.let {
            habitsService.registerUpdateListener(it.uid) { habits ->
                if (Build.VERSION.SDK_INT >= 34)
                    alarmManager.cancelAll()
                // if have notification permissions
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                    == PackageManager.PERMISSION_GRANTED) {
                    scheduleAllHabitsNotifications(context, habits)
                }
            }
        }
    }

    private fun scheduleAllHabitsNotifications(context: Context, habits: MutableList<Habit>) {
        Log.i("NOTI", "Scheduling notifications for ${habits.size} habits")
        for(i in 0 until habits.size)
            scheduleNotification(context, i, habits[i])
    }

    private fun scheduleNotification(context: Context, id : Int, habit: Habit) {
        // create the intent to the dashboard activity
        val intent = Intent(context, HabitNotification::class.java).apply {
            putExtra(
                "habit",
                habit
            )
        }
        // sets the intent that start the service who build the notification according to the habit
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val nextNoti = habit.frequency.getTimeUntilNextNotificationMillis()
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + nextNoti,
            pendingIntent
        )
    }
}