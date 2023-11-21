package com.example.lifecoach_.controllers.activities_controllers.activity_dashboard

import android.content.Context
import android.util.Log
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
        val file = File.createTempFile(UUID.randomUUID().toString(), ".jpg", filesDir)
        Log.i("USERIMAGE", "File creation result ${file.createNewFile()}")
        val destUri = FileProvider
            .getUriForFile(
                context,
                context.packageName + ".fileprovider",
                file)

        Log.i("USERIMAGE", "URI: ${destUri.toString()}")

        usersService.userListener(uid, file) {newUser ->
            Log.i("USERIMAGE", "Got User Image Update")
            if (newUser != null) {
                newUser.picture = destUri.toString()
            }
            callback(newUser)
        }
    }
}