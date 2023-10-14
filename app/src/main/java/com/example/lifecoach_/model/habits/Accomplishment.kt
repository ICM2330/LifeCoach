package com.example.lifecoach_.model.habits

import java.io.Serializable
import java.util.Date

class Accomplishment(
    var date: Date,
    var accomplishment: Int
) : Serializable {
}