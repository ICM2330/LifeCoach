package com.example.lifecoach_.model.habits

import android.location.Location
import java.time.Duration
import java.time.format.DateTimeFormatter
import java.util.Optional

class RunningHabit(
    name: String,
    history: List<DateTimeFormatter>,
    frequency: Frecuency,
    location: Optional<Location>,
    objetive : Duration,
    accomplishment : List<Duration>,
    var distance : Long

) : TimeControlHabit(name, history, frequency, location, objetive, accomplishment){

    override fun toString(): String {
        return "RunningHabit(distance=$distance)"
    }
}