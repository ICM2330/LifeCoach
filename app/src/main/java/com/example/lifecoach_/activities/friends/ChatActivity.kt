package com.example.lifecoach_.activities.friends

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.example.lifecoach_.R
import com.example.lifecoach_.activities.ProfileActivity
import com.example.lifecoach_.activities.habits.CreateHabitsActivity
import com.example.lifecoach_.adapters.MessageAdapter
import com.example.lifecoach_.databinding.ActivityChatBinding
import com.example.lifecoach_.databinding.ActivityProfileBinding
import com.example.lifecoach_.model.Friend
import com.example.lifecoach_.model.User
import com.example.lifecoach_.model.messages.MediaMessage
import com.example.lifecoach_.model.messages.MessageApp
import com.example.lifecoach_.model.messages.TextMessage
import java.util.Date

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val friend = intent.getSerializableExtra("friend") as Friend
        binding.cFriendName.text = friend.user.username

        binding.cFriendProfile.setOnClickListener {
            val intent = Intent(this, FriendActivity::class.java)
            intent.putExtra("friend", friend)
            startActivity(intent)
        }

        val adapter = MessageAdapter(this, friend.chat)
        binding.cmessages.adapter = adapter
    }
}
