package com.example.lifecoach_.services.firebase

import com.example.lifecoach_.model.habits.Habit
import com.example.lifecoach_.repositories.HabitRepository

class HabitsService {
    private val habitRepository: HabitRepository = HabitRepository()

    fun addHabit(habit: Habit, callback: (Habit) -> Unit) {
        // Agrega el Hábito
        habitRepository.addHabit(habit) {
            // TODO: Agrega todos los cumplimientos (Accomplishment)s relacionados
            // Retorna el hábito con el ID obtenido de Firebase
            callback(habit)
        }
    }
}