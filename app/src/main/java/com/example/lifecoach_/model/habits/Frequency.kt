package com.example.lifecoach_.model.habits

import java.io.Serializable

class Frequency(
    var notiHour: Int,
    var notiMinute: Int,
    var days: MutableList<Int>
) : Serializable {
    override fun toString(): String {
        return "Frequency(Hour=$notiHour:$notiMinute, Days=$days)"
    }
}