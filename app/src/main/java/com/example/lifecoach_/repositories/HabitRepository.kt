package com.example.lifecoach_.repositories

import android.util.Log
import com.example.lifecoach_.mappers.HabitMapper
import com.example.lifecoach_.mappers.firebase.HabitsResultMapper
import com.example.lifecoach_.model.habits.Habit
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HabitRepository {
    private val db = Firebase.firestore
    private val habitsRef = db.collection("habits")

    private val habitMapper: HabitMapper = HabitMapper()
    private val habitsResultMapper = HabitsResultMapper()

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
        // Mapear HashMap del Habito
        val habitHashMap = habitMapper.habitToMap(habit)

        // Buscar Documento Actual en FireStore
        if (habit.id != null) {
            habitsRef.document(habit.id!!).set(habitHashMap)
                .addOnSuccessListener {
                    callback(habit)
                }
        } else {
            Log.e("HABITSAVE", "No se puede actualizar un hábito sin su id")
        }
    }

    fun registerUpdateListener(uid: String, callback: (MutableList<Habit>) -> Unit) {
        val habits = mutableListOf<Habit>()
        habitsRef.whereEqualTo("uid", uid).get()
            .addOnSuccessListener {query ->
                habitsResultMapper.resultToHabitList(query, callback)
            }
    }
}