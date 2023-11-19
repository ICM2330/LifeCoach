package com.example.lifecoach_.mappers.firebase

import com.example.lifecoach_.mappers.HabitMapper
import com.example.lifecoach_.model.habits.Habit
import com.google.firebase.firestore.QuerySnapshot

class HabitsResultMapper {
    val habitMapper = HabitMapper()

    fun resultToHabitList(
        query: QuerySnapshot,
        callback: (MutableList<Habit>) -> Unit
    ) {
        val habits = mutableListOf<Habit>()
        query.documents.forEach { doc ->
            // Mapear HashMap a Objeto
            val habit = doc.data?.let { habitMapper.mapToHabit(doc.id, it) }

            // Agregar a la Lista
            if (habit != null) {
                habits.add(habit)
            }

            // TODO: Escuchar a Actualización de Accomps
        }

        // Retornar la Lista
        callback(habits)
    }
}