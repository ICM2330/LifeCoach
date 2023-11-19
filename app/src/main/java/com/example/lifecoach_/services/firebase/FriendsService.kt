package com.example.lifecoach_.services.firebase

import com.example.lifecoach_.mappers.converters.UserFriendMapper
import com.example.lifecoach_.model.Friend
import com.example.lifecoach_.model.messages.MessageApp
import com.example.lifecoach_.repositories.MsgRepository
import com.example.lifecoach_.repositories.UserRepository

class FriendsService {
    private val userRepository = UserRepository()
    private val userFriendMapper = UserFriendMapper()

    private val msgRepository = MsgRepository()

    fun registerAllFriendsListener(callback: (MutableList<Friend>) -> Unit) {
        userRepository.registerAllUsersListener { users ->
            // Mapear todos los usuarios a amigos
            val friends = mutableListOf<Friend>()
            users.forEach {user ->
                friends.add(userFriendMapper.userToFriend(user))
            }

            // TODO: Registrar Listener y Petición para los Mensajes

            // Retornar Lista de Amigos
            callback(friends)
        }
    }

    fun registerMessageListener(
        from: String,
        to: String,
        callback: (MutableList<MessageApp>) -> Unit
    ) {

    }

    fun sendMessage(from: String, to: String, msg: String, callback: () -> Unit) {
        msgRepository.sendMessage(from, to, msg, callback)
    }
}