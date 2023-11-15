package com.example.lifecoach_.services.firebase

import com.example.lifecoach_.model.User

class UsersService {
    fun saveUser(user: User, callback: () -> Unit) {
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