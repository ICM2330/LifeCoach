package com.example.lifecoach_.model.habits

import java.io.Serializable
import java.time.Duration
import java.time.format.DateTimeFormatter
import java.util.Date

class Frecuency (
    var start : Date,
    var period : Duration
):Serializable {

    override fun toString(): String {
        return "Frecuency(start=$start, period=$period)"
    }
}