package com.example.lifecoach_.Model

import android.location.Location
import com.example.lifecoach_.Habit
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