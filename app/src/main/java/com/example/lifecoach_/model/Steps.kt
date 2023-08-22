package com.example.lifecoach_.model

import java.io.Serializable
import java.util.Date

class Steps (
    var date : Date,
    var quantity : Int
) : Serializable {

    override fun toString(): String {
        return "Steps(date=$date, quantity=$quantity)"
    }
}