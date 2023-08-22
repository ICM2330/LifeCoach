package com.example.lifecoach_.model.habits

import android.location.Location
import java.time.Duration
import java.time.format.DateTimeFormatter
import java.util.Optional

class RunningHabit(
    name: String,
    history : MutableList <DateTimeFormatter>,
    frequency: Frecuency,
    locationStart: Location ?,
    locationFinish : Location ?,
    objetive : Duration,
    accomplishment : List<Duration>,
    var distance : Long

) : TimeControlHabit(name, history, frequency, objetive, accomplishment){

    override fun toString(): String {
        return "RunningHabit(distance=$distance)"
    }
}