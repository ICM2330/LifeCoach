package com.example.lifecoach_.Model

import java.time.Duration
import java.time.format.DateTimeFormatter

class Frecuency (
    var start : DateTimeFormatter,
    var period : Duration
) {

    override fun toString(): String {
        return "Frecuency(start=$start, period=$period)"
    }
}