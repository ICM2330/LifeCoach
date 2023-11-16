package com.example.lifecoach_.repositories

import com.example.lifecoach_.mappers.UserMapper
import com.example.lifecoach_.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserRepository {
    private val db: FirebaseFirestore = Firebase.firestore
    private val userMapper: UserMapper = UserMapper()

    fun saveUser(
        user: User,
        callback: () -> Unit
    ) {
        val userRef = db.collection("users")
        userRef.add(
            userMapper.userToMap(user)
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