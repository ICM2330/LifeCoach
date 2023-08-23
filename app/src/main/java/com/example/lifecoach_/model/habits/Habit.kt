package com.example.lifecoach_.model.habits

import android.location.Location
import java.time.format.DateTimeFormatter

open class Habit(
    var name: String,
    var history: MutableList<DateTimeFormatter>,
    var frecuency: Frecuency,
) {
}