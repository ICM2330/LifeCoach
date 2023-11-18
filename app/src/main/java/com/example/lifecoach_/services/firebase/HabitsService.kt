package com.example.lifecoach_.services.firebase

import com.example.lifecoach_.model.habits.Accomplishment
import com.example.lifecoach_.model.habits.Habit
import com.example.lifecoach_.repositories.AccompRepository
import com.example.lifecoach_.repositories.HabitRepository

class HabitsService {
    private val habitRepository: HabitRepository = HabitRepository()
    private val accompRepository: AccompRepository = AccompRepository()

    fun addOrUpdateHabit(habit: Habit, callback: (Habit) -> Unit) {
        if (habit.id != null) {
            updateHabit(habit, callback)
        } else {
            addHabit(habit, callback)
        }
    }

    private fun addHabit(habit: Habit, callback: (Habit) -> Unit) {
        // Agrega el Hábito
        habitRepository.addHabit(habit) {habitWithID: Habit ->
            // Agrega todos los cumplimientos (Accomplishment)s relacionados
            habitWithID.id?.let {hid: String ->
                addOrUpdateAccomps(habitWithID.accomplishment, hid)
                {
                    // Anexa los Accomps con IDs
                    habitWithID.accomplishment = it

                    // Retorna el Hábito con IDs
                    callback(habitWithID)
                }
            }
        }
    }

    private fun updateHabit(habit: Habit, callback: (Habit) -> Unit) {
        // Actualizar Documento del Habito
        habitRepository.updateHabit(habit) {habitUpdate: Habit ->
            addOrUpdateAccomps(habitUpdate.accomplishment, habitUpdate.id!!) {
                    updatedAccomps: MutableList<Accomplishment> ->
                habitUpdate.accomplishment = updatedAccomps
                callback(habitUpdate)
            }
        }
    }

    private fun addOrUpdateAccomps(
        accomps: MutableList<Accomplishment>,
        habitId: String,
        callback: (MutableList<Accomplishment>) -> Unit) {
        val accompsWithID = mutableListOf<Accomplishment>()

        // Actualizar o Agregar cada Accomplishment
        accomps.forEach {acomp: Accomplishment ->
            // Actualizar o Agregar Accomplishment
            addOrUpdateAccomp(acomp, habitId) {
                    updatedAcomp: Accomplishment ->
                accompsWithID.add(updatedAcomp)

                if (accompsWithID.size == accomps.size) {
                    callback(accompsWithID)
                }
            }
        }

        // Si no tiene accomps retorna de una vez
        accomps.ifEmpty {
            callback(accomps)
        }
    }

    private fun addOrUpdateAccomp(
        acomp: Accomplishment,
        habitId: String,
        callback: (Accomplishment) -> Unit
    ) {
        if (acomp.id != null) {
            // Actualizar Accomplishment
            accompRepository.updateAccomp(acomp, habitId, callback)
        } else {
            accompRepository.addAccomp(acomp, habitId, callback)
        }
    }
}