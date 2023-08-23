package com.example.lifecoach_.model.habits

import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.format.DateTimeFormatter
import java.util.Optional

@RequiresApi(Build.VERSION_CODES.O)
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