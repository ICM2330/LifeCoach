package com.example.lifecoach_.mappers

import com.example.lifecoach_.model.habits.Habit
import com.google.gson.Gson

class HabitMapper {
    private val gson = Gson()
    fun habitToMap(habit: Habit): HashMap<String, Any?> {
        return hashMapOf<String, Any?>(
            "name" to habit.name,
            "freq_hour" to habit.frequency.notiHour,
            "freq_min" to habit.frequency.notiMinute,
            "freq_days" to gson.toJson(habit.frequency.days)
        )
    }
}