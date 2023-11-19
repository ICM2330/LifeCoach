package com.example.lifecoach_.repositories

import com.example.lifecoach_.mappers.UserMapper
import com.example.lifecoach_.mappers.firebase.UsersResultMapper
import com.example.lifecoach_.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserRepository {
    private val db: FirebaseFirestore = Firebase.firestore
    private val userMapper: UserMapper = UserMapper()
    private val userRef = db.collection("users")

    private val usersResultMapper = UsersResultMapper()

    fun findUserByEmail(email: String, callback: (QuerySnapshot) -> Unit) {
        userRef.whereEqualTo("email", email).get()
            .addOnSuccessListener {
                callback(it)
            }
    }

    fun saveUser(
        user: User,
        picRef: String?,
        callback: () -> Unit
    ) {
        userRef.add(
            userMapper.userToMap(user, picRef)
        ).addOnSuccessListener {
            callback()
        }
    }

    fun updateUser(
        docId: String,
        user: User,
        picRef: String?,
        callback: () -> Unit
    ) {
        userRef.document(docId).get()
            .addOnSuccessListener {doc ->
                userRef.document(docId).set(
                    userMapper.userToMap(user, doc.data?.get("picture") as String)
                )
                    .addOnSuccessListener {
                        callback()
                    }
            }
    }

    fun getAllUsers(callback: (MutableList<User>) -> Unit) {
        userRef.get()
            .addOnSuccessListener {query ->
                // Mapear todos los documentos en una lista de usuarios
                val users = usersResultMapper.resultToUsers(query)

                // Retornar la lista de usuarios
                callback(users)
            }
    }
}