package com.example.lifecoach_.Model

import android.net.Uri
import java.io.Serializable
import java.util.Date

class TextMessage (timeStamp : Date,
                   fileAttached : Uri
) : MessageApp(timeStamp), Serializable {
}