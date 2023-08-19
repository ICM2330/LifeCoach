package com.example.lifecoach_.Model

import java.io.Serializable
import java.util.Date

open class MessageApp (
    var timeStamp : Date
): Serializable{

    override fun toString(): String {
        return "MessageApp(timeStamp=$timeStamp)"
    }
}