package com.example.lifecoach_.mappers.converters

import com.example.lifecoach_.model.Friend
import com.example.lifecoach_.model.User
import com.example.lifecoach_.model.messages.MessageApp

class UserFriendMapper {
    fun userToFriend(user: User): Friend {
        return Friend(
            user,
            mutableListOf<MessageApp>()
        )
    }
}