package com.example.lifecoach_.controllers.activities_controllers.activity_dashboard

import com.example.lifecoach_.model.User
import com.example.lifecoach_.model.habits.Habit
import com.example.lifecoach_.services.firebase.HabitsService

class DashBoardHabitsDataController {
    private val habitsService: HabitsService = HabitsService()

    fun addHabit(habit: Habit, callback: (Habit) -> Unit) {
        habitsService.addOrUpdateHabit(habit, callback)
    }

    fun updateHabit(habit: Habit, callback: (Habit) -> Unit) {
        habitsService.addOrUpdateHabit(habit, callback)
    }

    fun updatesListener(uid: String, callback: (MutableList<Habit>) -> Unit) {
        habitsService.registerUpdateListener(uid, callback)
    }
}