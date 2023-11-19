package com.example.lifecoach_.mappers.firebase

import com.example.lifecoach_.model.habits.Habit
import com.google.firebase.firestore.QuerySnapshot

class HabitsResultMapper {
    fun resultToHabitList(
        query: QuerySnapshot,
        callback: (MutableList<Habit>) -> Unit
    ) {
        query.documents.forEach { doc ->
            // TODO: Mapear HashMap a Objeto
            // TODO: Agregar a la Lista
            // TODO: Escuchar a Actualización de Accomps
        }

        // TODO: Retornar la Lista
    }
}