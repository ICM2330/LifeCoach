package com.example.lifecoach_.activities.friends

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lifecoach_.adapters.MessageAdapter
import com.example.lifecoach_.databinding.ActivityChatBinding
import com.example.lifecoach_.model.Friend

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
