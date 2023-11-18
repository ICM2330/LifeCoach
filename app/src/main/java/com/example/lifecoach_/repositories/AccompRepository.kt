package com.example.lifecoach_.repositories

import com.example.lifecoach_.mappers.AccompMapper
import com.example.lifecoach_.model.habits.Accomplishment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AccompRepository {
    private val db = Firebase.firestore
    private val accompRef = db.collection("accomps")

    private val accompMapper = AccompMapper()

    fun addAccomp(accomplishment: Accomplishment, habitId: String, callback: (accomplishment: Accomplishment) -> Unit) {
        // Mapear Accomplishment con ID
        val accompHashMap = accompMapper.accompToMap(accomplishment, habitId)

        // TODO: Guardar Accomplishment
        // TODO: Retornar Accomplishment con ID
    }
}