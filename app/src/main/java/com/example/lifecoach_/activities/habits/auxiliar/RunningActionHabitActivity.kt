package com.example.lifecoach_.activities.habits.auxiliar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import com.example.lifecoach_.R
import com.example.lifecoach_.databinding.ActivityRunningActionHabitBinding

class RunningActionHabitActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRunningActionHabitBinding
    private var running = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunningActionHabitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        manageButtons()
    }

    private fun manageButtons() {
        binding.actionsRunningButton.setOnClickListener {
            if (!running){
                // Begin running
                running = true
                binding.chronometerRunning.base = SystemClock.elapsedRealtime()
                binding.chronometerRunning.start()

                // Change button text
                binding.actionsRunningButton.text = "Detener"
            }
            else{
                // Stop running
                running = false
                binding.chronometerRunning.stop()

                // Change button text
                binding.actionsRunningButton.text = "Iniciar"
            }
        }
    }
}