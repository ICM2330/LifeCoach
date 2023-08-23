package com.example.lifecoach_.activities.habits

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lifecoach_.R
import com.example.lifecoach_.activities.ProfileActivity
import com.example.lifecoach_.activities.friends.ChatMenuActivity
import com.example.lifecoach_.activities.habits.creation.GenericHabitCreationActivity
import com.example.lifecoach_.activities.habits.creation.RunningHabitCreationActivity
import com.example.lifecoach_.activities.habits.creation.StepHabitCreationActivity
import com.example.lifecoach_.activities.habits.creation.TimeHabitCreationActivity
import com.example.lifecoach_.databinding.ActivityCreateHabitsBinding

class CreateHabitsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateHabitsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateHabitsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        manageButtons()
    }

    private fun manageButtons(){
        timeHabit()
        runHabit()
        muscleHabit()
        stepsHabit()
        featuredHabit()
    }

    private fun timeHabit(){
         binding.timeControlButton.setOnClickListener {
             startActivity(Intent(this, TimeHabitCreationActivity::class.java))
         }
    }

    fun runHabit(){
        binding.runHabitCreateButton.setOnClickListener {
            startActivity(Intent(this, RunningHabitCreationActivity::class.java))
        }
    }

    fun muscleHabit(){
        binding.musculationCreationHabit.setOnClickListener {

        }
    }

    fun stepsHabit(){
        binding.dailyStepsCreation.setOnClickListener {
            startActivity(Intent(this, StepHabitCreationActivity::class.java))
        }
    }

    fun featuredHabit(){
        binding.featuredHabitCreation.setOnClickListener {
            startActivity(Intent(this, GenericHabitCreationActivity::class.java))
        }
    }
}