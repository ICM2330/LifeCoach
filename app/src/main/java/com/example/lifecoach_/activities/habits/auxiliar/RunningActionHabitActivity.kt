package com.example.lifecoach_.activities.habits.auxiliar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import com.example.lifecoach_.R
import com.example.lifecoach_.databinding.ActivityRunningActionHabitBinding

class RunningActionHabitActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRunningActionHabitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunningActionHabitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        manageButtons()
    }

    private fun manageButtons() {
        binding.actionsRunningButton.setOnClickListener {
            if (binding.actionsRunningButton.text == "Iniciar"){
                binding.chronometerRunning.base = SystemClock.elapsedRealtime()
                binding.chronometerRunning.start()

                // Change button text
                binding.actionsRunningButton.text = "Detener"
            }
            else{
                // Stop Running
                val intent = Intent().apply {
                    binding.chronometerRunning.stop()
                    val minutesRanByChrono = (SystemClock.elapsedRealtime() - binding.chronometerRunning.base) / 60000
                    putExtra("minutesRan", minutesRanByChrono.toInt())
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }
    }
    
}