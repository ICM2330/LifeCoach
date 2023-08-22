package com.example.lifecoach_.model.habits

import android.location.Location
import java.time.format.DateTimeFormatter
import java.util.Optional

open class Habit (
    var name : String,
    var history : List <DateTimeFormatter>,
    var frecuency : Frecuency,
    location : Optional<Location>
    ) {
}