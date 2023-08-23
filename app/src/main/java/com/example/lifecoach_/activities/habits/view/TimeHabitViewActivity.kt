package com.example.lifecoach_.activities.habits.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lifecoach_.R
import com.example.lifecoach_.activities.habits.creation.TimeHabitCreationActivity
import com.example.lifecoach_.databinding.ActivityTimeHabitViewBinding

class TimeHabitViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTimeHabitViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimeHabitViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEditar.setOnClickListener {
            startActivity(Intent(this, TimeHabitCreationActivity::class.java))
        }
    }
}