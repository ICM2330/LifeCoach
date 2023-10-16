package com.example.lifecoach_.activities.habits.auxiliar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import com.example.lifecoach_.R
import com.example.lifecoach_.databinding.ActivityTimeHabitRegisterBinding

class TimeHabitRegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityTimeHabitRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimeHabitRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        manageButtons()
    }

    private fun manageButtons() {
        binding.startButton.setOnClickListener {
            if (binding.startButton.text == "Iniciar") {
                binding.temp.base = SystemClock.elapsedRealtime()
                binding.temp.start()

                // Change button text
                binding.startButton.text = "Detener"
            } else {
                // Stop Running
                binding.temp.stop()
                val minutesRanByChrono =
                    (SystemClock.elapsedRealtime() - binding.temp.base) / 60000
                val intent = Intent().apply {
                    putExtra("minutes", minutesRanByChrono.toInt())
                }
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }
}