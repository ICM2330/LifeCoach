package com.example.lifecoach_.model.messages

import java.io.Serializable
import java.util.Date

class TextMessage (
    forwarded : Boolean,
    timeStamp : Date,
    var text : String
) : MessageApp(forwarded, timeStamp), Serializable {
    override fun toString(): String {
        return text
    }
}