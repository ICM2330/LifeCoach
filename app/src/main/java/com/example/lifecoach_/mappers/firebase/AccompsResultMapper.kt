package com.example.lifecoach_.mappers.firebase

import com.example.lifecoach_.model.habits.Accomplishment
import com.google.firebase.firestore.QuerySnapshot

class AccompsResultMapper {
    fun resultToAccompsList(
        querySnapshot: QuerySnapshot,
        callback: (MutableList<Accomplishment>) -> Unit
    ) {

    }
}