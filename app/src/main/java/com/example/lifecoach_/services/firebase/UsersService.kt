package com.example.lifecoach_.services.firebase

import android.net.Uri
import android.util.Log
import com.example.lifecoach_.model.User
import com.example.lifecoach_.repositories.PicRepository
import com.example.lifecoach_.repositories.UserRepository

class UsersService {
    private val userRepository: UserRepository = UserRepository()
    private val picRepository: PicRepository = PicRepository()

    fun registerUser(user: User, picUri: Uri?, callback: () -> Unit) {
        Log.i("REGISTER", "Looking for user ${user.email}")
        tryFindUser(user) {docId: String? ->
            if (docId == null) {
                Log.i("REGISTER", "User ${user.email} not found")
                saveNewUser(user, picUri, callback)
            } else {
                Log.i("REGISTER", "User ${user.email} found at $docId")
                updateUserDocument(docId, user, null, callback)
            }
        }
    }

    fun updateUser(user: User, picUri: Uri?, callback: () -> Unit) {
        tryFindUser(user) {docId ->
            if (docId == null) {
                saveNewUser(user, picUri, callback)
            } else {
                updateUserDocument(docId, user, picUri, callback)
            }
        }
    }

    fun userListener(uid: String, callback: (User?) -> Unit) {
        userRepository.registerSingleUserListener(uid, callback)
    }

    private fun saveNewUser(
        user: User,
        picUri: Uri?,
        callback: () -> Unit
    ) {
        if (picUri != null) {
            picRepository.saveImage(picUri) { picRef: String ->
                userRepository.saveUser(user, picRef, callback)
            }
        } else {
            userRepository.saveUser(user, null, callback)
        }
    }

    private fun updateUserDocument(
        docId: String,
        user: User,
        picUri: Uri?,
        callback: () -> Unit
    ) {
        if (picUri != null) {
            picRepository.saveImage(picUri) {picRef ->
                userRepository.updateUser(docId, user, picRef, callback)
            }
        } else {
            userRepository.updateUser(docId, user, null, callback)
        }
    }

    private fun tryFindUser(
        user: User,
        callback: (String?) -> Unit
    ) {
        userRepository.findUserByEmail(user.email) {
            try {
                callback(it.documents[0].id)
            } catch (e: Exception) {
                callback(null)
            }
        }
    }
}