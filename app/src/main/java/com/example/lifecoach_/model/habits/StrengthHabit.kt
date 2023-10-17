package com.example.lifecoach_.model.habits

import java.io.Serializable

class StrengthHabit(
    name: String,
    frequency: Frequency,
    var muscularGroup: String
) : Habit(name, frequency), Serializable {
    override fun doneToday() : Boolean {
        return if(lastAccIsToday()) accomplishment[accomplishment.size-1].accomplishment >= 1
        else false
    }
}