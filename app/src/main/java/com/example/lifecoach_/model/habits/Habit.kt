package com.example.lifecoach_.model.habits

import java.io.Serializable

open class Habit(
    var name: String,
    var frequency: Frequency
) : Serializable {
    var accomplishment = mutableListOf<Accomplishment>()
}