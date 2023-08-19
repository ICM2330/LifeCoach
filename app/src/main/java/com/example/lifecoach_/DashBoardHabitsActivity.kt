package com.example.lifecoach_

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lifecoach_.databinding.ActivityDashBoardHabitsBinding

class DashBoardHabitsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDashBoardHabitsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDashBoardHabitsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}