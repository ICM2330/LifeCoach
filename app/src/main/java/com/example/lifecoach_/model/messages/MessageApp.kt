package com.example.lifecoach_.model.messages

import java.io.Serializable
import java.util.Date

open class MessageApp (
    var forwarded : Boolean,
    var timeStamp : Date
): Serializable{

    override fun toString(): String {
        return "MessageApp(timeStamp=$timeStamp)"
    }
}