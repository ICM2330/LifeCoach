package com.example.lifecoach_.mappers

import com.example.lifecoach_.model.User

class UserMapper {
    fun userToMap(user: User, picRef: String?): HashMap<String, Any?> {
        if (picRef != null) {
            return hashMapOf<String, Any?>(
                "uid" to user.uid,
                "name" to user.name,
                "username" to user.username,
                "email" to user.email,
                "phone" to user.phone,
                "picture" to picRef
            )
        } else {
            return hashMapOf<String, Any?>(
                "uid" to user.uid,
                "name" to user.name,
                "username" to user.username,
                "email" to user.email,
                "phone" to user.phone
            )
        }
    }
}