package com.example.lifecoach_.controllers.activities_controllers.activity_chat_menu

import com.example.lifecoach_.model.Friend
import com.example.lifecoach_.services.firebase.FriendsService

class ChatMenuDataController {
    private val friendsService = FriendsService()

    fun registerFriendsListener(callback: (MutableList<Friend>) -> Unit) {
        friendsService.registerAllFriendsListener(callback)
    }
}