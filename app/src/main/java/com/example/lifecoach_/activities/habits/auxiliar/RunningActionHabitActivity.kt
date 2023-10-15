package com.example.lifecoach_.activities.habits.auxiliar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lifecoach_.R
import com.example.lifecoach_.databinding.ActivityRunningActionHabitBinding

class RunningActionHabitActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRunningActionHabitBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunningActionHabitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Do a chronometer since the activity is loaded
        
    }
}