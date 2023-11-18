package com.example.lifecoach_.repositories

import com.example.lifecoach_.model.habits.Accomplishment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AccompRepository {
    private val db = Firebase.firestore
    private val accompRef = db.collection("accomps")

    fun addAccomp(accomplishment: Accomplishment, habitId: String, callback: (accomplishment: Accomplishment) -> Unit) {
        // TODO: Mapear Accomplishment con ID
        // TODO: Guardar Accomplishment
        // TODO: Retornar Accomplishment con ID
    }
}