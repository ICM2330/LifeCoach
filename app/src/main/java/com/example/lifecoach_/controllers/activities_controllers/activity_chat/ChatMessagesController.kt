package com.example.lifecoach_.controllers.activities_controllers.activity_chat

import com.example.lifecoach_.model.Friend
import com.example.lifecoach_.model.messages.MessageApp
import com.example.lifecoach_.services.firebase.FriendsService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChatMessagesController {
    private val auth: FirebaseAuth = Firebase.auth

    private val friendsService = FriendsService()

    fun registerMessagesListener(
        from: Friend,
        callback: (MutableList<MessageApp>) -> Unit
    ) {
        from.user.uid?.let { fromUid ->
            auth.currentUser?.let { currentUser ->
                friendsService.registerMessageListener(fromUid, currentUser.uid) {
                    msgs ->
                    callback(msgs)
                }
            }
        }
    }

    fun sendMessage(message: String, to: Friend, callback: () -> Unit) {
        auth.currentUser?.let { to.user.uid?.let { it1 ->
            friendsService.sendMessage(it.uid,
                it1, message, callback)
        } }
    }
}