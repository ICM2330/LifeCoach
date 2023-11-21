package com.example.lifecoach_.model.habits

import android.util.Log
import java.io.Serializable
import java.util.Calendar
import java.util.Date

class Frequency(
    var notiHour: Int,
    var notiMinute: Int,
    var days: MutableList<Int>
) : Serializable {
    override fun toString(): String {
        val textDays = listOf("L", "M", "X", "J", "V", "S", "D")
        var ret = ""
        for (day in days) ret += textDays[day] + " "
        ret += "a las ${hourString()}"
        return ret
    }

    public fun hourString(): String {
        var ret = ""
        if (notiHour % 12 == 0) ret += "12:"
        else ret += "${notiHour % 12}:"
        if (notiMinute < 10) ret += "0$notiMinute "
        else ret += "$notiMinute "
        if (notiHour < 12) ret += "am"
        else ret += "pm"
        return ret
    }

    fun getTimeUntilNextNotificationMillis(): Long {
        val currentTimeMillis = System.currentTimeMillis()
        val currentCalendar = Calendar.getInstance()

        val notificationCalendar = Calendar.getInstance()
        notificationCalendar.set(Calendar.HOUR_OF_DAY, notiHour)
        notificationCalendar.set(Calendar.MINUTE, notiMinute)
        notificationCalendar.set(Calendar.SECOND, 0)
        notificationCalendar.set(Calendar.MILLISECOND, 0)

        Log.i("NOTIDAY", "now ${currentCalendar.time}")
        for (day in days) {
            notificationCalendar.set(Calendar.DAY_OF_WEEK, toCalendarDays[day])
            Log.i("NOTIDAY", "checking ${notificationCalendar.time}")
            if (currentCalendar.before(notificationCalendar)) {
                Log.i("NOTIDAY", "${notificationCalendar.time}")
                Log.i("NOTIDAY", "${notificationCalendar.timeInMillis - currentTimeMillis}")
                return notificationCalendar.timeInMillis - currentTimeMillis
            }
        }

        notificationCalendar.add(Calendar.WEEK_OF_YEAR, 1)
        notificationCalendar.set(Calendar.DAY_OF_WEEK, toCalendarDays[days.first()])

        Log.i("NOTIDAY", "${notificationCalendar.time}")
        Log.i("NOTIDAY", "${notificationCalendar.timeInMillis - currentTimeMillis}")
        return notificationCalendar.timeInMillis - currentTimeMillis
    }

    companion object {
        val toCalendarDays = listOf(2, 3, 4, 5, 6, 7, 1)
    }
}