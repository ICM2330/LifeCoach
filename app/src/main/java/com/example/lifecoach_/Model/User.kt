package com.example.lifecoach_.Model

import android.media.Image
import android.telephony.PhoneNumberFormattingTextWatcher
import android.telephony.PhoneNumberUtils
import android.text.Editable
import java.io.Serializable

class User(
    var name: String,
    var username: String,
    var email: String,
    var phone: Long
):Serializable
{
    lateinit var picture: Image
    var dark_mode: Int = 0
    lateinit var steps: List<Steps>
    lateinit var friends : List<Friend>
    var loged: Boolean = false
    override fun toString(): String {
        return "User(name='$name', picture=$picture, username='$username', email='$email', phone=$phone, dark_mode=$dark_mode, steps=$steps, friends=$friends, loged=$loged)"
    }
}