package com.example.lifecoach_.model.habits

import java.io.Serializable

open class TimeControlHabit(
    name: String,
    frequency: Frequency,
    var objectiveMins: Int
) : Habit(name, frequency), Serializable {

    override fun doneToday(): Boolean {
        return if (lastAccIsToday()) accomplishment[accomplishment.size - 1].accomplishment >= objectiveMins
        else false
    }
}