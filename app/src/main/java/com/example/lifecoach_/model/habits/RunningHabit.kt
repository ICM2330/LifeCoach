package com.example.lifecoach_.model.habits

import java.io.Serializable

class RunningHabit(
    name: String,
    frequency: Frequency,
    objectiveMins: Int,
) : TimeControlHabit(name, frequency, objectiveMins), Serializable {
    var listDistances = mutableListOf<Int>()

    override fun doneToday() : Boolean {
        return if(lastAccIsToday()) accomplishment[accomplishment.size-1].accomplishment >= objectiveMins
        else false
    }
}