package com.example.lifecoach_.controllers.activities_controllers.activity_dashboard

import android.content.Context
import androidx.core.content.FileProvider
import com.example.lifecoach_.model.User
import com.example.lifecoach_.services.firebase.UsersService
import java.io.File
import java.util.UUID

class DashBoardUserController(
    val context: Context,
    val filesDir: File
) {
    private val usersService = UsersService()
    fun updatePictureListener(uid: String, callback: (User?) -> Unit) {
        val file = File(filesDir, UUID.randomUUID().toString() + ".jpg")
        val destUri = FileProvider
            .getUriForFile(
                context,
                context.packageName + ".fileprovider",
                file)

        usersService.userListener(uid, destUri) {newUser ->
            callback(newUser)
        }
    }
}