package com.example.lifecoach_.Model

import java.io.Serializable

class Friend (
    var user : User,
    var chat : List<MessageApp>
) : Serializable {
}