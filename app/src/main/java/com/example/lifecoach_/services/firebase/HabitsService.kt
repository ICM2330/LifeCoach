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
            habitWithID.id?.let {hid: String ->
                addsAccomps(habitWithID.accomplishment, hid)
                {
                    // Anexa los Accomps con IDs
                    habitWithID.accomplishment = it

                    // Retorna el Hábito con IDs
                    callback(habitWithID)
                }
            }
        }
    }

    private fun addsAccomps(
        accomps: MutableList<Accomplishment>,
        habitId: String,
        callback: (MutableList<Accomplishment>) -> Unit) {
        val accompsWithId = mutableListOf<Accomplishment>()
        accomps.forEachIndexed { index: Int, accomplishment: Accomplishment ->
            accompRepository.addAccomp(accomplishment, habitId)
            { accomplishmentWithID: Accomplishment ->
                accompsWithId.add(accomplishmentWithID)
                if (accompsWithId.size == accomps.size) {
                    // Retorna los Accomplishments con los IDS
                    callback(accompsWithId)
                }
            }
        }
    }
}