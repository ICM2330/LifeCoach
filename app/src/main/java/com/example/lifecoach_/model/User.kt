package com.example.lifecoach_.model

import com.example.lifecoach_.model.habits.Habit
import java.io.Serializable

class User(
    var name: String,
    var username: String,
    var email: String,
    var phone: Long
):Serializable
{
    var picture: String = ""
    var dark_mode: Int = 0
    var habits = mutableListOf<Habit>()
    lateinit var friends : List<Friend>
    override fun toString(): String {
        var p = ""
        if (this::picture.isInitialized) {
            p = " picture=$picture,"
        }
        return "User(name='$name',$p username='$username', email='$email', phone=$phone, dark_mode=$dark_mode)"
    }
}