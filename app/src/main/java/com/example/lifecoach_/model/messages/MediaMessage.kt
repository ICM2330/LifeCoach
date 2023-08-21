package com.example.lifecoach_.model.messages

import android.net.Uri
import java.io.Serializable
import java.util.Date

class MediaMessage (
    timeStamp : Date,
    var fileAttached : Uri
    ) : MessageApp(timeStamp), Serializable {

    override fun toString(): String {
        return "media"
    }
}