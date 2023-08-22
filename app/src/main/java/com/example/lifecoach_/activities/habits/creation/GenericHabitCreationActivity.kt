package com.example.lifecoach_.activities.habits.creation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lifecoach_.R
import com.example.lifecoach_.databinding.ActivityGenericHabitCreationBinding

class GenericHabitCreationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGenericHabitCreationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenericHabitCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCrear.setOnClickListener {
            finish()
        }
    }
}