package com.example.lifecoach_

import android.location.Location
import com.example.lifecoach_.Model.Frecuency
import java.time.format.DateTimeFormatter
import java.util.Optional

open class Habit (
    var name : String,
    var history : List <DateTimeFormatter>,
    var frecuency : Frecuency,
    location : Optional<Location>
    ) {
}