package com.example.lifecoach_.repositories

import android.net.Uri
import android.util.Log
import com.example.lifecoach_.mappers.UserMapper
import com.example.lifecoach_.mappers.firebase.UsersResultMapper
import com.example.lifecoach_.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.File

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
                val keepPicRef: String = if (picRef.isNullOrEmpty()) {
                    doc.data?.get("picture") as String
                } else {
                    picRef
                }
                userRef.document(docId).set(
                    userMapper.userToMap(user, keepPicRef)
                )
                    .addOnSuccessListener {
                        callback()
                    }
            }
    }

    fun registerSingleUserListener(
        uid: String,
        picDest: File?,
        callback: (User?) -> Unit
    ) {
        userRef.whereEqualTo("uid", uid)
            .addSnapshotListener { value, _ ->
                Log.i("USERIMAGE", "User update received")
                val userMap = value?.documents?.get(0)?.data
                if (userMap != null) {
                    userMapper.mapToUser(userMap, picDest) {user ->
                        callback(user)
                    }
                } else {
                    callback(null)
                }
            }
    }

    fun registerAllUsersListener(callback: (MutableList<User>) -> Unit) {
        userRef.addSnapshotListener { query, _ ->
            // Mapear todos los documentos en una lista de usuarios
            if (query != null) {
                usersResultMapper.resultToUsers(query) {users ->
                    // Retornar la lista de usuarios
                    callback(users)
                }
            }
        }
    }
}