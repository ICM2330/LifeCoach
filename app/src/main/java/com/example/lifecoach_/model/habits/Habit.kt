package com.example.lifecoach_.model.habits

import java.io.Serializable
import java.util.Calendar
import java.util.Date

open class Habit(
    var name: String,
    var frequency: Frequency
) : Serializable {
    var accomplishment = mutableListOf<Accomplishment>()

    fun shouldDoToday(): Boolean {
        var isToday = false

        val today = (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + 5) % 7
        for (day in frequency.days) {
            if (today == day) {
                isToday = true
                if (accomplishment.size == 0 || !lastAccIsToday()) {
                    accomplishment.add(Accomplishment(Date(), 0))
                }
                break
            }
        }

        return isToday
    }

    private fun lastAccIsToday(): Boolean {
        val date1 =accomplishment[accomplishment.size - 1].date
        val date2 = Date()
        val calendar1 = Calendar.getInstance()
        val calendar2 = Calendar.getInstance()
        calendar1.time = date1
        calendar2.time = date2

        return (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
                calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH))
    }

    fun setTodayAccomplishment(acc: Int) {
        if (accomplishment.size == 0) {
            accomplishment.add(Accomplishment(Date(), 0))
        }
        else if (!lastAccIsToday()) {
            accomplishment.add(Accomplishment(Date(), 0))
        }
        accomplishment[accomplishment.size - 1].accomplishment = acc
    }
}