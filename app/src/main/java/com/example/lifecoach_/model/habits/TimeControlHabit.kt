package com.example.lifecoach_.model.habits

import java.io.Serializable

open class TimeControlHabit(
    name: String,
    frequency: Frequency,
    var objectiveMins: Int
) : Habit(name, frequency), Serializable {
}