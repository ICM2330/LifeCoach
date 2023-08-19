package com.example.lifecoach_

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lifecoach_.databinding.ActivityChatMenuBinding

class ChatMenuActivity : AppCompatActivity() {
    private lateinit var binding : ActivityChatMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityChatMenuBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}