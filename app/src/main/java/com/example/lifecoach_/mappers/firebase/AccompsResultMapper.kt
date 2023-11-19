package com.example.lifecoach_.mappers.firebase

import com.example.lifecoach_.mappers.AccompMapper
import com.example.lifecoach_.model.habits.Accomplishment
import com.google.firebase.firestore.QuerySnapshot

class AccompsResultMapper {
    private val accompMapper = AccompMapper()

    fun resultToAccompsList(
        querySnapshot: QuerySnapshot
    ): MutableList<Accomplishment> {
        val accomps = mutableListOf<Accomplishment>()
        querySnapshot.documents.forEach { doc ->
            // TODO: Mapea de Documento a Accomplisment

            // TODO: Agrega el Accomplishment a la Lista
        }

        return accomps
    }
}