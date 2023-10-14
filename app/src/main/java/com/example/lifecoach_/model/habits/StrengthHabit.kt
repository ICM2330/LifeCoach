package com.example.lifecoach_.model.habits

import java.io.Serializable

class StrengthHabit(
    name: String,
    frequency: Frequency,
    var muscularGroup: String
) : Habit(name, frequency), Serializable {
}