package com.example.lifecoach_.mappers.firebase

import com.example.lifecoach_.mappers.HabitMapper
import com.example.lifecoach_.model.habits.Habit
import com.example.lifecoach_.repositories.AccompRepository
import com.google.firebase.firestore.QuerySnapshot

class HabitsResultMapper {
    val habitMapper = HabitMapper()
    val accompRepository = AccompRepository()

    fun resultToHabitList(
        query: QuerySnapshot,
        callback: (MutableList<Habit>) -> Unit
    ) {
        val habits = mutableListOf<Habit>()
        query.documents.forEach { doc ->
            // Mapear HashMap a Objeto
            val habit = doc.data?.let { habitMapper.mapToHabit(doc.id, it) }

            // Si se pudo mapear el Habito
            if (habit != null) {
                // Agregar a la Lista
                habits.add(habit)

                // Escuchar a Actualización de Accomps
                habit.id?.let { hid ->
                    accompRepository.registerListenerFor(hid) {
                        habit.accomplishment = it
                        callback(habits)
                    }
                }
            }
        }

        // Retornar la Lista
        callback(habits)
    }
}