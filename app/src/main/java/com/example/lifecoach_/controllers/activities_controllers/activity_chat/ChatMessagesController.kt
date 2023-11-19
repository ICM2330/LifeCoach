package com.example.lifecoach_.controllers.activities_controllers.activity_chat

import com.example.lifecoach_.model.Friend
import com.example.lifecoach_.model.messages.MessageApp
import com.example.lifecoach_.services.firebase.FriendsService
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChatMessagesController {
    private val friendsService = FriendsService()

    fun registerMessagesListener(
        from: Friend,
        callback: (MutableList<MessageApp>) -> Unit
    ) {
        from.user.uid?.let { fromUid ->
            Firebase.auth.currentUser?.let { currentUser ->
                friendsService.registerMessageListener(fromUid, currentUser.uid) {
                    msgs ->
                    callback(msgs)
                }
            }
        }
    }
}