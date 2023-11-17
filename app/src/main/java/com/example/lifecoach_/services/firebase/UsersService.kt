package com.example.lifecoach_.services.firebase

import android.net.Uri
import com.example.lifecoach_.model.User
import com.example.lifecoach_.repositories.PicRepository
import com.example.lifecoach_.repositories.UserRepository

class UsersService {
    private val userRepository: UserRepository = UserRepository()
    private val picRepository: PicRepository = PicRepository()

    fun registerUser(user: User, picUri: Uri?, callback: () -> Unit) {
        tryFindUser(user) {docId: String? ->
            if (docId == null) {
                saveNewUser(user, picUri, callback)
            } else {
                updateUser(docId, user, callback)
            }
        }
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

    private fun updateUser(
        docId: String,
        user: User,
        callback: () -> Unit
    ) {
        userRepository.updateUser(docId, user, callback)
    }

    private fun tryFindUser(
        user: User,
        callback: (String?) -> Unit
    ) {
        userRepository.findUserByEmail(user.email) {
            callback(it.documents[0].id)
        }
    }
}