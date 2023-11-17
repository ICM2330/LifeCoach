package com.example.lifecoach_.repositories

import com.example.lifecoach_.mappers.UserMapper
import com.example.lifecoach_.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserRepository {
    private val db: FirebaseFirestore = Firebase.firestore
    private val userMapper: UserMapper = UserMapper()
    private val userRef = db.collection("users")

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
        callback: () -> Unit
    ) {

    }
}