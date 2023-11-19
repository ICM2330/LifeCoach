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
        query.documents.forEach { doc ->
            // Mapear HashMap a Objeto
            val habit = doc.data?.let { habitMapper.mapToHabit(doc.id, it) }

            // TODO: Agregar a la Lista
            // TODO: Escuchar a Actualización de Accomps
        }

        // TODO: Retornar la Lista
    }
}