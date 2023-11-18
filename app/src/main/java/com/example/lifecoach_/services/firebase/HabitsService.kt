package com.example.lifecoach_.services.firebase

import com.example.lifecoach_.model.habits.Accomplishment
import com.example.lifecoach_.model.habits.Habit
import com.example.lifecoach_.repositories.AccompRepository
import com.example.lifecoach_.repositories.HabitRepository

class HabitsService {
    private val habitRepository: HabitRepository = HabitRepository()
    private val accompRepository: AccompRepository = AccompRepository()

    fun addHabit(habit: Habit, callback: (Habit) -> Unit) {
        // Agrega el Hábito
        habitRepository.addHabit(habit) {habitWithID: Habit ->
            // Agrega todos los cumplimientos (Accomplishment)s relacionados
            habitWithID.accomplishment.forEach { accomplishment: Accomplishment ->
                habitWithID.id?.let { hid: String ->
                    accompRepository.addAccomp(accomplishment, hid) {

                    }
                }
            }
            // Retorna el hábito con el ID obtenido de Firebase
            callback(habitWithID)
        }
    }
}