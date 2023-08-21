package com.example.lifecoach_

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lifecoach_.adapter.FriendChatAdapter
import com.example.lifecoach_.databinding.ActivityChatMenuBinding
import com.example.lifecoach_.model.Friend
import com.example.lifecoach_.model.User
import com.example.lifecoach_.model.messages.MediaMessage
import com.example.lifecoach_.model.messages.MessageApp
import com.example.lifecoach_.model.messages.TextMessage
import java.util.Date

class ChatMenuActivity : AppCompatActivity() {
    private lateinit var binding : ActivityChatMenuBinding
    private lateinit var friends : MutableList<Friend>

    val array = Array<String>(30) { i -> if (i % 2 == 0)
        "Hola" else "Mundo" }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityChatMenuBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        friends = createFriends()
        val adapter = FriendChatAdapter(this, friends)
        binding.cmChats.adapter = adapter
    }

    fun createFriends(): MutableList<Friend> {
        val nombres = listOf(
            "Esteban Salazar",
            "Javier Moyano",
            "Juan Camilo Sanchez",
            "Sofia Cespedes",
            "Cristiano Ronaldo",
            "Lewis Hamilton",
            "Kyllian Mbappe"
        )
        val messages = listOf(
            "Hola Juan, ya registre mis habitos de hoy",
            "Hola bro, Hoy toca brazo en el gimnasio?",
            "Listo, casi que no! Logre los 700 pasos del dia",
            "",
            "Eu corri de casa para o estádio",
            "¡New race record, today I ran 55 kilometers!",
            "J'ai déjà lu une heure aujourd'hui"
        )
        val friends: MutableList<Friend> = mutableListOf()
        for (i in nombres.indices) {
            val message: List<MessageApp> =
                if (messages[i].isEmpty()) listOf(MediaMessage(Date(), Uri.EMPTY))
                else listOf(TextMessage(Date(), messages[i]))
            friends.add(Friend(User("", nombres[i], "", 0), message))
        }
        return friends
    }
}