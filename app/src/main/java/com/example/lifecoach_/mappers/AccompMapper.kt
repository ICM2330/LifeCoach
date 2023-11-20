package com.example.lifecoach_.mappers

import com.example.lifecoach_.model.habits.Accomplishment
import com.google.firebase.Timestamp
import java.util.Date

class AccompMapper {
    fun accompToMap(accomplishment: Accomplishment, habitId: String): HashMap<String, Any?> {
        return hashMapOf(
            "amount" to accomplishment.accomplishment,
            "date" to accomplishment.date,
            "habit_id" to habitId
        )
    }

    fun mapToAccomp(docId: String, accompMap: Map<String, Any?>): Accomplishment {
        val accomp = Accomplishment(
            (accompMap["date"] as Timestamp).toDate(),
            (accompMap["amount"] as Long).toInt()
        )

        accomp.id = docId

        return accomp
    }
}