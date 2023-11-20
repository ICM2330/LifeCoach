package com.example.lifecoach_.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lifecoach_.R
import com.example.lifecoach_.databinding.ActivityFollowFriendBinding
import com.example.lifecoach_.model.Friend
import com.example.lifecoach_.model.User

class FollowFriendActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFollowFriendBinding
    private lateinit var userFriend : User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFollowFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the friend's from the intent
        val friend = intent.getSerializableExtra("friend") as Friend
        userFriend = friend.user

        binding.userId.text = userFriend.username
    }
}