package com.example.lifecoach_.model.habits

import java.io.Serializable

class RunningHabit(
    id: String?,
    name: String,
    frequency: Frequency,
    objectiveMins: Int,
) : TimeControlHabit(id, name, frequency, objectiveMins), Serializable {
    var listDistances = mutableListOf<Int>()

    override fun doneToday() : Boolean {
        return if(lastAccIsToday()) accomplishment[accomplishment.size-1].accomplishment >= objectiveMins
        else false
    }
}