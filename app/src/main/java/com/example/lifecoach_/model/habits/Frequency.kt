package com.example.lifecoach_.model.habits

import java.io.Serializable

class Frequency(
    var notiHour: Int,
    var notiMinute: Int,
    var days: MutableList<Int>
) : Serializable {
    override fun toString(): String {
        val textDays = listOf("L", "M", "X", "J", "V", "S", "D")
        var ret = ""
        for (day in days) ret += textDays[day] + " "
        ret += "a las ${hourString()}"
        return ret
    }

    public fun hourString(): String {
        var ret = ""
        if (notiHour % 12 == 0) ret += "12:"
        else ret += "${notiHour % 12}:"
        if (notiMinute < 10) ret += "0$notiMinute "
        else ret+= "$notiMinute "
        if(notiHour < 12) ret += "am"
        else ret += "pm"
        return ret
    }
}