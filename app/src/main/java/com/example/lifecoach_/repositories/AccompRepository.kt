package com.example.lifecoach_.repositories

import com.example.lifecoach_.mappers.AccompMapper
import com.example.lifecoach_.mappers.firebase.AccompsResultMapper
import com.example.lifecoach_.model.habits.Accomplishment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AccompRepository {
    private val db = Firebase.firestore
    private val accompRef = db.collection("accomps")

    private val accompMapper = AccompMapper()
    private val accompsResultMapper = AccompsResultMapper()

    fun addAccomp(accomplishment: Accomplishment, habitId: String, callback: (accomplishment: Accomplishment) -> Unit) {
        // Mapear Accomplishment con ID
        val accompHashMap = accompMapper.accompToMap(accomplishment, habitId)

        // Guardar Accomplishment
        accompRef.add(accompHashMap)
            .addOnSuccessListener {
                // Retornar Accomplishment con ID
                accomplishment.id = it.id
                callback(accomplishment)
            }
    }

    fun updateAccomp(
        accomplishment: Accomplishment,
        habitId: String,
        callback: (accomplishment: Accomplishment) -> Unit
    ) {
        // Mapear Accomp
        val accompHashMap = accompMapper.accompToMap(accomplishment, habitId)
        // Buscar Documento del Accomp Actual en FB
        val doc = accompRef.document(accomplishment.id!!) // Se utiliza "!!" porque no se debe llamar a este método con el id nulo
        // Actualizar
        doc.set(accompHashMap)
            .addOnSuccessListener {
                callback(accomplishment)
            }
    }

    fun registerListenerFor(
        habitId: String,
        callback: (accomplishment: MutableList<Accomplishment>) -> Unit
    ) {
        accompRef.whereEqualTo("habit_id", habitId).get()
            .addOnSuccessListener {query ->
                // Mapea todos los Accomps en Objetos y Retorna
                accompsResultMapper.resultToAccompsList(query, callback)
            }
    }
}