package com.example.lifecoach_.activities.habits

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.lifecoach_.activities.habits.creation.GenericHabitCreationActivity
import com.example.lifecoach_.activities.habits.creation.MuscularHabitCreationActivity
import com.example.lifecoach_.activities.habits.creation.RunningHabitCreationActivity
import com.example.lifecoach_.activities.habits.creation.StepHabitCreationActivity
import com.example.lifecoach_.activities.habits.creation.TimeHabitCreationActivity
import com.example.lifecoach_.databinding.ActivityCreateHabitsBinding
import com.example.lifecoach_.model.habits.Habit


class CreateHabitsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateHabitsBinding

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val returnIntent = Intent().apply{
                    putExtra("habit", intent?.getSerializableExtra("habit") as Habit)
                }
                setResult(RESULT_OK, returnIntent)
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateHabitsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        manageButtons()
    }

    private fun manageButtons() {
        // Time control habit
        binding.timeControlButton.setOnClickListener {
            startForResult.launch(Intent(this, TimeHabitCreationActivity::class.java))
        }

        // Running habit
        binding.runHabitCreateButton.setOnClickListener {
            startForResult.launch(Intent(this, RunningHabitCreationActivity::class.java))
        }

        // Strength Habit
        binding.musculationCreationHabit.setOnClickListener {
            startForResult.launch(Intent(this, MuscularHabitCreationActivity::class.java))
        }

        // Steps habit
        binding.dailyStepsCreation.setOnClickListener {
            startForResult.launch(Intent(this, StepHabitCreationActivity::class.java))
        }

        // Generic habit
        binding.featuredHabitCreation.setOnClickListener {
            startForResult.launch(Intent(this, GenericHabitCreationActivity::class.java))
        }
    }
}