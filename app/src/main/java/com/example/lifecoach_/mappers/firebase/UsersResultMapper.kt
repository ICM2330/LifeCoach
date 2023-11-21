package com.example.lifecoach_.mappers.firebase

import com.example.lifecoach_.mappers.UserMapper
import com.example.lifecoach_.model.User
import com.google.firebase.firestore.QuerySnapshot

class UsersResultMapper {
    private val userMapper = UserMapper()

    fun resultToUsers(
        query: QuerySnapshot,
        callback: (MutableList<User>) -> Unit
    ) {
        val users = mutableListOf<User>()
        query.documents.forEach {doc ->
            // Mapear Documento a Usuario
            doc.data?.let {userMap ->
                userMapper.mapToUser(userMap, null) {user ->
                    // Agregar Usuario a la Lista
                    users.add(user)

                    if (query.documents.size == users.size) {
                        callback(users)
                    }
                }
            }
        }

        query.documents.ifEmpty {
            callback(users)
        }
    }
}