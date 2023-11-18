package com.example.lifecoach_.repositories

import android.util.Log
import com.example.lifecoach_.mappers.HabitMapper
import com.example.lifecoach_.model.habits.Habit
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HabitRepository {
    private val db = Firebase.firestore
    private val habitsRef = db.collection("habits")

    private val habitMapper: HabitMapper = HabitMapper()

    fun addHabit(habit: Habit, callback: (Habit) -> Unit) {
        // Mapea el Habito a un HashMap
        Log.i("HABITSTORE", "Mapeando Habito ...")
        val habitHashMap = habitMapper.habitToMap(habit)

        // Carga el HashMap en Firebase
        Log.i("HABITSTORE", "Cargando HashMap en Firestore")
        habitsRef.add(habitHashMap)
            .addOnSuccessListener {
                // Retorna el Habito con el ID
                Log.i("HABITSTORE", "Retornando habito con el ID")
                habit.id = it.id
                callback(habit)
            }
    }

    fun updateHabit(habit: Habit, callback: (Habit) -> Unit) {
        // TODO: Mapear HashMap del Habito
        // TODO: Buscar Documento Actual en FireStore
    }
}