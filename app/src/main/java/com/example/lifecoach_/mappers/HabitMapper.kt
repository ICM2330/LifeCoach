package com.example.lifecoach_.mappers

import com.example.lifecoach_.model.habits.Frequency
import com.example.lifecoach_.model.habits.Habit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HabitMapper {
    private val gson = Gson()
    fun habitToMap(habit: Habit, uid: String): HashMap<String, Any?> {
        return hashMapOf<String, Any?>(
            "name" to habit.name,
            "freq_hour" to habit.frequency.notiHour,
            "freq_min" to habit.frequency.notiMinute,
            "freq_days" to gson.toJson(habit.frequency.days),
            "uid" to uid
        )
    }

    fun mapToHabit(docId: String, habitHashMap: Map<String, Any?>): Habit {
        return Habit(
            docId,
            habitHashMap["name"] as String,
            Frequency(
                (habitHashMap["freq_hour"] as Long).toInt(),
                (habitHashMap["freq_min"] as Long).toInt(),
                gson.fromJson(habitHashMap["freq_days"] as String, object: TypeToken<MutableList<Int>>(){}.type)
            )
        )
    }
}