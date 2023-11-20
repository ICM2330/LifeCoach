package com.example.lifecoach_.controllers.activities_controllers.activity_main

import android.net.Uri
import com.example.lifecoach_.model.User
import com.example.lifecoach_.services.firebase.UsersService

class MainActivityRegisterController {
    private val usersService: UsersService = UsersService()

    fun registerUser(user: User, picUri: Uri?, callback: (User) -> Unit) {
        usersService.registerUser(user, picUri) {
            callback(user)
        }
    }
}