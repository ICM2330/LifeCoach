package com.example.lifecoach_.activities.habits.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lifecoach_.R
import com.example.lifecoach_.databinding.ActivityRunningHabitRegisterBinding

class RunningHabitRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRunningHabitRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunningHabitRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSalir.setOnClickListener {
            finish()
        }
    }
}