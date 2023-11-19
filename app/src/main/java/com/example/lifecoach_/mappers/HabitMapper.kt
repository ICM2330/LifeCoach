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

            "type" to "time",
            "obj" to habit.objectiveMins
        )
    }

    private fun runningHabitToMap(habit: RunningHabit, uid: String): HashMap<String, Any?> {
        return hashMapOf<String, Any?>(
            "name" to habit.name,
            "freq_hour" to habit.frequency.notiHour,
            "freq_min" to habit.frequency.notiMinute,
            "freq_days" to gson.toJson(habit.frequency.days),
            "uid" to uid,

            "type" to "running",
            "obj" to habit.objectiveMins,
            "distances" to gson.toJson(habit.listDistances)
        )
    }

    fun mapToHabit(docId: String, habitHashMap: Map<String, Any?>): Habit {
        return when (habitHashMap["type"]) {
            "running" -> mapToRunningHabit(docId, habitHashMap)
            "time" -> mapToTimeHabit(docId, habitHashMap)
            "steps" -> mapToStepsHabit(docId, habitHashMap)
            "strength" -> mapToGeneralHabit(docId, habitHashMap)
            else -> mapToGeneralHabit(docId, habitHashMap)
        }
    }

    private fun mapToRunningHabit(
        docId: String,
        habitHashMap: Map<String, Any?>
    ): RunningHabit {
        val rh = RunningHabit(
            docId,
            habitHashMap["name"] as String,
            Frequency(
                (habitHashMap["freq_hour"] as Long).toInt(),
                (habitHashMap["freq_min"] as Long).toInt(),
                gson.fromJson(habitHashMap["freq_days"] as String, object: TypeToken<MutableList<Int>>(){}.type)
            ),
            (habitHashMap["obj"] as Long).toInt()
        )

        rh.listDistances = gson.fromJson(
            habitHashMap["distances"] as String,
            object: TypeToken<MutableList<Int>>(){}.type
        )

        return rh
    }

    private fun mapToTimeHabit(
        docId: String,
        habitHashMap: Map<String, Any?>
    ): TimeControlHabit {
        return TimeControlHabit(
            docId,
            habitHashMap["name"] as String,
            Frequency(
                (habitHashMap["freq_hour"] as Long).toInt(),
                (habitHashMap["freq_min"] as Long).toInt(),
                gson.fromJson(habitHashMap["freq_days"] as String, object: TypeToken<MutableList<Int>>(){}.type)
            ),
            (habitHashMap["obj"] as Long).toInt()
        )
    }

    private fun mapToStepsHabit(
        docId: String,
        habitHashMap: Map<String, Any?>
    ): StepsHabit {
        return StepsHabit(
            docId,
            habitHashMap["name"] as String,
            Frequency(
                (habitHashMap["freq_hour"] as Long).toInt(),
                (habitHashMap["freq_min"] as Long).toInt(),
                gson.fromJson(habitHashMap["freq_days"] as String, object: TypeToken<MutableList<Int>>(){}.type)
            ),
            (habitHashMap["obj"] as Long).toInt()
        )
    }

    private fun mapToGeneralHabit(
        docId: String,
        habitHashMap: Map<String, Any?>
    ): Habit {
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