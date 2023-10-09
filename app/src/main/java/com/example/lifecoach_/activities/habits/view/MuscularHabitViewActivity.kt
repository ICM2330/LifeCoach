package com.example.lifecoach_.activities.habits.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lifecoach_.activities.habits.auxiliar.SearchGymForHabitActivity
import com.example.lifecoach_.databinding.ActivityMuscularHabitViewBinding

class MuscularHabitViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMuscularHabitViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMuscularHabitViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        manageButtons()
    }

    private fun manageButtons(){
        manageButtonGoToSearchGymForHabitActivity()
    }

    private fun manageButtonGoToSearchGymForHabitActivity(){
        binding.buttonGimnasiosSearch.setOnClickListener {
            startActivity(Intent(baseContext, SearchGymForHabitActivity::class.java))
        }
    }
}