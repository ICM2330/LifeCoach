package com.example.lifecoach_.activities.friends

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lifecoach_.adapters.FriendChatAdapter
import com.example.lifecoach_.databinding.ActivityChatMenuBinding
import com.example.lifecoach_.model.Friend
import com.example.lifecoach_.model.User
import com.example.lifecoach_.model.messages.MessageApp
import com.example.lifecoach_.model.messages.TextMessage
import java.util.Date

class ChatMenuActivity : AppCompatActivity() {
    private lateinit var binding : ActivityChatMenuBinding
    private lateinit var friends : MutableList<Friend>

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityChatMenuBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        friends = createFriends()
        val adapter = FriendChatAdapter(this, friends)
        binding.cmChats.adapter = adapter

        binding.cmChats.setOnItemClickListener { parent, view, position, id  ->
            val intent = Intent(baseContext, ChatActivity::class.java)
            intent.putExtra("friend", friends[position])
            startActivity(intent)
        }
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
        val lastMessage = listOf(
            "Hola Juan, ya registre mis habitos de hoy",
            "Hola bro, Hoy toca brazo en el gimnasio?",
            "Listo, casi que no! Logre los 700 pasos del dia",
            "",
            "Eu corri de casa para o estádio",
            "¡New race record, today I ran 55 kilometers!",
            "J'ai déjà lu une heure aujourd'hui"
        )

        val messages = mutableListOf<MessageApp>(
            TextMessage(true,Date(),"Hola"),
            TextMessage(false,Date(),"Hola, como estas?"),
            TextMessage(true,Date(),"Bien"),
            TextMessage(true,Date(),"y tu?"),
            TextMessage(false,Date(),"Bien"),
            TextMessage(true,Date(),"Te queria contar que complete mis habitos de hoy, todo gracias a esta app!! \n\n\nTe comparto su logo para que se la muestres a tus amigos!")
        )

        val friends: MutableList<Friend> = mutableListOf()
        for (i in nombres.indices) {
            val chat = messages.toMutableList()
            if(lastMessage[i] != "")
                chat.add(TextMessage(false, Date(), lastMessage[i]))
            friends.add(Friend(User(null, nombres[i], "${nombres[i]}${nombres[i].length}", "${nombres[i]}@gmail.com", 0), chat))
        }
        return friends
    }
}