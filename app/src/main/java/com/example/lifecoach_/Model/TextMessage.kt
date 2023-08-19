package com.example.lifecoach_.Model

import android.net.Uri
import java.io.Serializable
import java.util.Date

class TextMessage (timeStamp : Date,
                   var text : String
) : MessageApp(timeStamp), Serializable {
    override fun toString(): String {
        return "TextMessage(text='$text')"
    }
}