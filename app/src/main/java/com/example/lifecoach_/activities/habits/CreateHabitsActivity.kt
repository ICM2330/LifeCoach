package com.example.lifecoach_.activities.habits

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lifecoach_.R
import com.example.lifecoach_.activities.ProfileActivity
import com.example.lifecoach_.activities.friends.ChatMenuActivity
import com.example.lifecoach_.databinding.ActivityCreateHabitsBinding

class CreateHabitsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateHabitsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateHabitsBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    fun manageButtons(){
        bottomNavigationBarManagement(binding)
        timeHabit()
        runHabit()
        muscleHabit()
        stepsHabit()
        featuredHabit()
    }

    fun timeHabit(){
         binding.timeControlButton.setOnClickListener {

         }
    }

    fun runHabit(){
        binding.runHabitCreateButton.setOnClickListener {

        }
    }

    fun muscleHabit(){
        binding.musculationCreationHabit.setOnClickListener {

        }
    }

    fun stepsHabit(){
        binding.dailyStepsCreation.setOnClickListener {

        }
    }

    fun featuredHabit(){
        binding.featuredHabitCreation.setOnClickListener {

        }
    }

    fun bottomNavigationBarManagement(binding: ActivityCreateHabitsBinding) {
        binding.bottomNavigationViewCreate.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuProfile -> {
                    //Do an intent with the profile activity
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menuChat -> {
                    // Do an intent with the chat activity
                    val intent = Intent(this, ChatMenuActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menuHabits -> {
                    // Do an intent with the dashboard of habits activity
                    val intent = Intent(this, CreateHabitsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
}