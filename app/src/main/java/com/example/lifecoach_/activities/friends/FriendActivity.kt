package com.example.lifecoach_.activities.friends

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lifecoach_.databinding.ActivityFriendBinding
import com.example.lifecoach_.model.Friend

class FriendActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFriendBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityFriendBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var friend = intent.getSerializableExtra("friend") as Friend
        binding.fFriendName.text = friend.user.name
        binding.fFriendUsername.text = "@${friend.user.username}"
    }
}