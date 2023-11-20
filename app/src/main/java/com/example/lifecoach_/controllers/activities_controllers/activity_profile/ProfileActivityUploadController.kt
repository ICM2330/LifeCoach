package com.example.lifecoach_.controllers.activities_controllers.activity_profile

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.example.lifecoach_.model.User
import com.example.lifecoach_.services.firebase.UsersService

class ProfileActivityUploadController(val context: Context) {
    private val usersService = UsersService()

    fun updateUser(user: User) {
        Toast.makeText(
            context,
            "Actualizando Información ...",
            Toast.LENGTH_LONG
        ).show()

        val uri = Uri.parse(user.picture)
        usersService.updateUser(user, uri) {
            Toast.makeText(
                context,
                "Usuario Actualizado",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}