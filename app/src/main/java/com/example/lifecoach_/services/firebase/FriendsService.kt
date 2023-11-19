package com.example.lifecoach_.services.firebase

import com.example.lifecoach_.mappers.converters.UserFriendMapper
import com.example.lifecoach_.model.Friend
import com.example.lifecoach_.repositories.UserRepository

class FriendsService {
    private val userRepository = UserRepository()
    private val userFriendMapper = UserFriendMapper()

    fun getAllFriends(callback: (MutableList<Friend>) -> Unit) {
        userRepository.getAllUsers {users ->
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
}