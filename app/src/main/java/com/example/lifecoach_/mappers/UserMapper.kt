package com.example.lifecoach_.mappers

import com.example.lifecoach_.model.User

class UserMapper {
    fun userToMap(user: User, picRef: String?): HashMap<String, Any?> {
        return hashMapOf<String, Any?>(
            "uid" to user.uid,
            "name" to user.name,
            "username" to user.username,
            "email" to user.email,
            "phone" to user.phone,
            "picture" to (picRef ?: "")
        )
    }

    fun mapToUser(userMap: Map<String, Any?>,
                  callback: (User) -> Unit) {
        val user = User(
            userMap["uid"] as String,
            userMap["name"] as String,
            userMap["username"] as String,
            userMap["email"] as String,
            userMap["phone"] as Long
        )

        // TODO: Descarga de la Foto del Usuario

        callback(user)
    }
}