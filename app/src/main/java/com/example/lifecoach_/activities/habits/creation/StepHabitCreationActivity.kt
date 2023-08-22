package com.example.lifecoach_.activities.habits.creation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lifecoach_.R
import com.example.lifecoach_.databinding.ActivityStepHabitCreationBinding

class StepHabitCreationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStepHabitCreationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStepHabitCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCrear.setOnClickListener {
            finish()
        }
    }
}