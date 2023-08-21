package com.example.lifecoach_.model.messages

import java.io.Serializable
import java.util.Date

class TextMessage (timeStamp : Date,
                   var text : String
) : MessageApp(timeStamp), Serializable {
    override fun toString(): String {
        return text
    }
}