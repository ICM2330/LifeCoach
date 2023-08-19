package com.example.lifecoach_.Model

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