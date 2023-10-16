package com.example.lifecoach_.model.habits

import java.io.Serializable

class StepsHabit(
    name: String,
    frequency: Frequency,
    var objectiveSteps: Int
) : Habit(name, frequency), Serializable {

    override fun doneToday() : Boolean {
        return if(lastAccIsToday()) accomplishment[accomplishment.size-1].accomplishment >= objectiveSteps
        else false
    }
}