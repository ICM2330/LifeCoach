package com.example.lifecoach_.mappers

import com.example.lifecoach_.model.habits.Accomplishment

class AccompMapper {
    fun accompToMap(accomplishment: Accomplishment, habitId: String): HashMap<String, Any?> {
        return hashMapOf(
            "amount" to accomplishment.accomplishment,
            "date" to accomplishment.date,
            "habit_id" to habitId
        )
    }
}