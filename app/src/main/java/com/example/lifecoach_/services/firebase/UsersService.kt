package com.example.lifecoach_.services.firebase

import com.example.lifecoach_.model.User
import com.example.lifecoach_.repositories.UserRepository

class UsersService {
    private val userRepository: UserRepository = UserRepository()

    fun registerUser(user: User, callback: () -> Unit) {
        tryFindUser(user) {docId: String? ->
            if (docId == null) {
                saveNewUser(user, callback)
            } else {
                updateUser(docId, user, callback)
            }
        }
    }

    private fun saveNewUser(
        user: User,
        callback: () -> Unit
    ) {
        userRepository.saveUser(user, callback)
    }

    fun updateUser(
        docId: String,
        user: User,
        callback: () -> Unit
    ) {

    }

    private fun tryFindUser(
        user: User,
        callback: (String?) -> Unit
    ) {

    }
}