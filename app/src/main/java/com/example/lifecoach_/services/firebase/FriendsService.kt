package com.example.lifecoach_.services.firebase

import com.example.lifecoach_.model.Friend
import com.example.lifecoach_.repositories.UserRepository

class FriendsService {
    private val userRepository = UserRepository()

    fun getAllFriends(callback: (MutableList<Friend>) -> Unit) {
        userRepository.getAllUsers {
            // TODO: Mapear todos los usuarios a amigos

            // TODO: Registrar Listener y Petición para los Mensajes

            // TODO: Retornar Lista de Amigos
        }
    }
}