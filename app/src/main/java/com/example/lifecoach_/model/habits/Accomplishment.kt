package com.example.lifecoach_.model.habits

import java.io.Serializable
import java.util.Date

data class Accomplishment(
    var date: Date,
    var accomplishment: Int
) : Serializable {
    var id: String? = null
}