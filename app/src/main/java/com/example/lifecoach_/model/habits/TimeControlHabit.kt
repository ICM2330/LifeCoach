package com.example.lifecoach_.model.habits

import android.location.Location
import java.time.Duration
import java.time.format.DateTimeFormatter
import java.util.Optional

open class TimeControlHabit(
    name: String,
    history: List<DateTimeFormatter>,
    frequency: Frecuency,
    location: Optional<Location>,
    var objective : Duration,
    var accomplishment : List<Duration>

) : Habit(name, history, frequency, location) {

    override fun toString(): String {
        return "TimeControlHabit(objective=$objective, accomplishment=$accomplishment)"
    }
}