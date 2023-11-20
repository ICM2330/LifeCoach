package com.example.lifecoach_.mappers

import android.net.Uri
import com.example.lifecoach_.model.User
import com.example.lifecoach_.repositories.PicRepository
import java.io.File

class UserMapper {
    private val picRepository = PicRepository()

    fun userToMap(user: User, picRef: String?): HashMap<String, Any?> {
        return hashMapOf<String, Any?>(
            "uid" to user.uid,
            "name" to user.name,
            "username" to user.username,
            "email" to user.email,
            "phone" to user.phone,
            "latitude" to user.latitude,
            "longitude" to user.longitude,
            "picture" to (picRef ?: "")
        )
    }

    fun mapToUser(userMap: Map<String, Any?>,
                  picDest: File?,
                  callback: (User) -> Unit) {
        val user = User(
            userMap["uid"] as String,
            userMap["name"] as String,
            userMap["username"] as String,
            userMap["email"] as String,
            userMap["phone"] as Long,
            userMap["latitude"] as Double,
            userMap["longitude"] as Double
        )

        if (picDest != null) {
            picRepository.downloadImage(userMap["picture"] as String, picDest) {
                callback(user)
            }
        } else {
            callback(user)
        }
    }
}