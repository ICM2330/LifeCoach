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
            // Mapea de Documento a Accomplisment
            val accomp = doc.data?.let { accompMapper.mapToAccomp(doc.id, it) }

            // Agrega el Accomplishment a la Lista
            if (accomp != null) {
                accomps.add(accomp)
            }
        }

        return accomps
    }
}