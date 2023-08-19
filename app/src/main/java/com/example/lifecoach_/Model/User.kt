package com.example.lifecoach_.Model

import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.Phone
import java.io.Serializable

class User (
    var name : String,
    var picture : Uri,
    var username : String,
    var email : String,
    var phone : Phone,
    var dark_mode : Int,
    var steps : List<Steps>,
    var loged : Boolean):
    Serializable
{
    override fun toString(): String {
        return "User(name='$name', picture=$picture, username='$username', email='$email', phone=$phone, dark_mode=$dark_mode, steps=$steps, loged=$loged)"
    }

}