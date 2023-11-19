package com.example.lifecoach_.mappers

import com.example.lifecoach_.model.habits.Frequency
import com.example.lifecoach_.model.habits.Habit
import com.example.lifecoach_.model.habits.RunningHabit
import com.example.lifecoach_.model.habits.StepsHabit
import com.example.lifecoach_.model.habits.StrengthHabit
import com.example.lifecoach_.model.habits.TimeControlHabit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HabitMapper {
    private val gson = Gson()
    fun habitToMap(habit: Habit, uid: String): HashMap<String, Any?> {
        return when {
            habit is RunningHabit -> runningHabitToMap(habit, uid)
            habit is TimeControlHabit -> timeHabitToMap(habit, uid)
            habit is StepsHabit -> stepsHabitToMap(habit, uid)
            habit is StrengthHabit -> strengthHabitToMap(habit, uid)
            else -> generalHabitToMap(habit, uid)
        }
    }

    private fun generalHabitToMap(habit: Habit, uid: String)
        : HashMap<String, Any?> {
        return hashMapOf<String, Any?>(
            "name" to habit.name,
            "freq_hour" to habit.frequency.notiHour,
            "freq_min" to habit.frequency.notiMinute,
            "freq_days" to gson.toJson(habit.frequency.days),
            "uid" to uid,
            "type" to "general"
        )
    }

    private fun strengthHabitToMap(habit: StrengthHabit, uid: String)
            : HashMap<String, Any?> {
        return hashMapOf<String, Any?>(
            "name" to habit.name,
            "freq_hour" to habit.frequency.notiHour,
            "freq_min" to habit.frequency.notiMinute,
            "freq_days" to gson.toJson(habit.frequency.days),
            "uid" to uid,

            "type" to "strength",
            "muscular_group" to habit.muscularGroup
        )
    }

    private fun stepsHabitToMap(habit: StepsHabit, uid: String)
            : HashMap<String, Any?> {
        return hashMapOf<String, Any?>(
            "name" to habit.name,
            "freq_hour" to habit.frequency.notiHour,
            "freq_min" to habit.frequency.notiMinute,
            "freq_days" to gson.toJson(habit.frequency.days),
            "uid" to uid,

            "type" to "steps",
            "obj" to habit.objectiveSteps
        )
    }

    private fun timeHabitToMap(habit: TimeControlHabit, uid: String): HashMap<String, Any?> {
        return hashMapOf<String, Any?>(
            "name" to habit.name,
            "freq_hour" to habit.frequency.notiHour,
            "freq_min" to habit.frequency.notiMinute,
            "freq_days" to gson.toJson(habit.frequency.days),
            "uid" to uid,
            "type" to "time"
        )
    }

    private fun runningHabitToMap(habit: RunningHabit, uid: String): HashMap<String, Any?> {
        return hashMapOf<String, Any?>(
            "name" to habit.name,
            "freq_hour" to habit.frequency.notiHour,
            "freq_min" to habit.frequency.notiMinute,
            "freq_days" to gson.toJson(habit.frequency.days),
            "uid" to uid,
            "type" to "running"
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