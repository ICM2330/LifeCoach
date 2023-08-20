package com.example.lifecoach_.model.habits

import android.location.Location
import java.io.Serializable
import java.time.format.DateTimeFormatter
import java.util.Optional

class StepsHabit(
    name: String,
    history: List<DateTimeFormatter>,
    frequency: Frecuency,
    location: Optional<Location>,
    var objective : Int,
    var accomplishment : List<Int>

    ) : Habit(name, history, frequency, location), Serializable {
    override fun toString(): String {
        return "StepsHabit(objective=$objective, accomplishment=$accomplishment)"
    }
}