package com.example.lifecoach_.services.firebase

import android.util.Log
import com.example.lifecoach_.model.User
import com.example.lifecoach_.model.habits.Accomplishment
import com.example.lifecoach_.model.habits.Habit
import com.example.lifecoach_.repositories.AccompRepository
import com.example.lifecoach_.repositories.HabitRepository

class HabitsService {
    private val habitRepository: HabitRepository = HabitRepository()
    private val accompRepository: AccompRepository = AccompRepository()

    fun addOrUpdateHabit(habit: Habit, user: User, callback: (Habit) -> Unit) {
        if (user.uid != null) {
            if (habit.id != null) {
                Log.i("UPDATE", "Actualizando habito")
                updateHabit(habit, user.uid!!, callback)
            } else {
                Log.i("UPDATE", "Agregando habito")
                addHabit(habit, user.uid!!, callback)
            }
        } else {
            Log.e("HABITSAVE", "No se pudo guardar el hábito debido a que no se tenía el ID del usuario")
        }
    }

    fun registerUpdateListener(uid: String, callback: (MutableList<Habit>) -> Unit) {
        habitRepository.registerUpdateListener(uid, callback)
    }

    private fun addHabit(habit: Habit, uid: String, callback: (Habit) -> Unit) {
        // Agrega el Hábito
        habitRepository.addHabit(habit, uid) {habitWithID: Habit ->
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

    private fun updateHabit(habit: Habit, uid: String, callback: (Habit) -> Unit) {
        // Actualizar Documento del Habito
        habitRepository.updateHabit(habit, uid) {habitUpdate: Habit ->
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