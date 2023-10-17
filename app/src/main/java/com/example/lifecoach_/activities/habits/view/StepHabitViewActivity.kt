package com.example.lifecoach_.activities.habits.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.lifecoach_.R
import com.example.lifecoach_.databinding.ActivityStepHabitViewBinding
import com.example.lifecoach_.model.habits.Accomplishment
import com.example.lifecoach_.model.habits.StepsHabit
import com.example.lifecoach_.sensor_controllers.StepsController
import java.util.Date

class StepHabitViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStepHabitViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStepHabitViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        habit = intent.getSerializableExtra("habit") as StepsHabit
        startLists()
        displayHabitInfo()
        manageButtons()

        configureStepsController()
    }

    private lateinit var stepsController: StepsController

    private fun configureStepsController() {
        val perm = Manifest.permission.ACTIVITY_RECOGNITION

        val granted = {
            Log.i("STEPS", "Configuring Steps Controller")
            stepsController = StepsController.getStepsController()
            stepsController.configureStepSensor(baseContext)
            stepsController.registerStepsListener {
                binding.stepCount.text = it.toString()
            }
        }

        val denied = {
            val t = Toast.makeText(baseContext,
                "Se necesita el permiso de ver la actividad física. Por favor, activarlo desde la configuración.",
                Toast.LENGTH_LONG)
            t.show()
        }

        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                granted()
            } else {
                denied()
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            when {
                ContextCompat.checkSelfPermission(baseContext, perm)
                        == PackageManager.PERMISSION_GRANTED -> {
                    granted()
                }
                shouldShowRequestPermissionRationale(perm) -> {
                    denied()
                }
                else -> {
                    requestPermissionLauncher.launch(perm)
                }
            }
        } else {
            granted()
        }
    }

    private lateinit var habit: StepsHabit
    private var notificationDays = mutableListOf<TextView>()
    private var accomplishmentDays = mutableListOf<TextView>()

    private fun manageButtons() {
    }

    private fun startLists() {
        notificationDays = mutableListOf(
            binding.vshDay0,
            binding.vshDay1,
            binding.vshDay2,
            binding.vshDay3,
            binding.vshDay4,
            binding.vshDay5,
            binding.vshDay6
        )

        accomplishmentDays = mutableListOf(
            binding.vshADay0,
            binding.vshADay1,
            binding.vshADay2,
            binding.vshADay3,
            binding.vshADay4,
            binding.vshADay5,
            binding.vshADay6
        )
    }

    private fun displayHabitInfo() {
        binding.vshHabitName.text = habit.name
        val hour = "${habit.frequency.notiHour}:${habit.frequency.notiMinute}"
        binding.vshHour.text = hour

        // Days of notification
        for (day in habit.frequency.days) {
            notificationDays[day].setBackgroundColor(getColor(R.color.green1))
            notificationDays[day].setTextColor(getColor(R.color.white))
        }

        for (i in 0..6) {
            if (habit.accomplishment.size > i) {
                if (habit.accomplishment[habit.accomplishment.size - 1 - i].accomplishment >= habit.objectiveSteps)
                    accomplishmentDays[i].setBackgroundColor(getColor(R.color.green1))
                else accomplishmentDays[i].setBackgroundColor(getColor(R.color.red))
            } else break
        }
    }
}