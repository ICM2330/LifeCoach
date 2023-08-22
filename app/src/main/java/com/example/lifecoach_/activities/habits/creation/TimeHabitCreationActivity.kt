package com.example.lifecoach_.activities.habits.creation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lifecoach_.databinding.ActivityTimeHabitCreationBinding

class TimeHabitCreationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTimeHabitCreationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimeHabitCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCrear.setOnClickListener {
            finish()
        }
    }
}