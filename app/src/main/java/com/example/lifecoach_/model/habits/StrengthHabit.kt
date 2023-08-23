package com.example.lifecoach_.model.habits

import android.location.Location
import java.time.format.DateTimeFormatter
class StrengthHabit(
    name : String,
    history: MutableList<DateTimeFormatter>,
    frecuency: Frecuency,
    var muscularGroup : String,
    var accomplishment : List<Float>
): Habit(name, history, frecuency) {
}