package com.example.lifecoach_.mappers.firebase

import com.example.lifecoach_.model.User
import com.google.firebase.firestore.QuerySnapshot

class UsersResultMapper {
    fun resultToUsers(query: QuerySnapshot): MutableList<User> {
        val users = mutableListOf<User>()
        query.documents.forEach {
            // TODO: Mapear Documento a Usuario

            // TODO: Agregar Usuario a la Lista
        }

        return users
    }
}