package com.example.lifecoach_.Model

import java.io.Serializable

class Friend (
    var user : User,
    var chat : List<MessageApp>
) : Serializable {
    override fun toString(): String {
        return "Friend(user=$user, chat=$chat)"
    }
}