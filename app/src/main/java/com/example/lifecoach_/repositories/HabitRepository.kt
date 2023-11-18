package com.example.lifecoach_.repositories

import com.example.lifecoach_.mappers.HabitMapper
import com.example.lifecoach_.model.habits.Habit

class HabitRepository {
    private val habitMapper: HabitMapper = HabitMapper()

    fun addHabit(habit: Habit, callback: (Habit) -> Unit) {
        // Mapea el Habito a un HashMap
        val habitHashMap = habitMapper.habitToMap(habit)

        // TODO: Carga el HashMap en Firebase
        // TODO: Retorna el Habito con el ID
    }
}