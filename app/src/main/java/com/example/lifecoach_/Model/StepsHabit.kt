package com.example.lifecoach_.Model

import android.location.Location
import com.example.lifecoach_.Habit
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