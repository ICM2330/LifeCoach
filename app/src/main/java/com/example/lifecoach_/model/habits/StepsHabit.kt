package com.example.lifecoach_.model.habits

import android.location.Location
import java.io.Serializable
import java.time.format.DateTimeFormatter

class StepsHabit(
    name: String,
    history: MutableList<DateTimeFormatter>,
    frequency: Frecuency,
    var objective : Int,
    var accomplishment : List<Int>

) : Habit(name, history, frequency), Serializable {
    override fun toString(): String {
        return "StepsHabit(objective=$objective, accomplishment=$accomplishment)"
    }
}