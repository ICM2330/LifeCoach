package com.example.lifecoach_.activities.habits.creation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lifecoach_.databinding.ActivityRunningHabitCreationBinding

class RunningHabitCreationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRunningHabitCreationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunningHabitCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPartida.setOnClickListener {
            // startActivity(Intent(this, TODO: Colocar Vista de Mapa que retorne una ubicación))
        }

        binding.btnLlegada.setOnClickListener {
            // startActivity(Intent(this, TODO: Colocar Vista de Mapa que retorne una ubicación))
        }
    }
}