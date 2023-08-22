package com.example.lifecoach_.model

import com.example.lifecoach_.model.messages.MessageApp
import java.io.Serializable

class Friend (
    var user : User,
    var chat : MutableList<MessageApp>
) : Serializable {
    override fun toString(): String {
        return "Friend(user=$user, chat=$chat)"
    }
}