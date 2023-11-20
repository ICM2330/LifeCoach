package com.example.lifecoach_.activities.habits.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.activity.addCallback
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.AppCompatActivity
import com.example.lifecoach_.R
import com.example.lifecoach_.activities.habits.creation.StepHabitCreationActivity
import com.example.lifecoach_.databinding.ActivityStepHabitViewBinding
import com.example.lifecoach_.model.habits.StepsHabit
import com.example.lifecoach_.controllers.sensor_controllers.StepsController
import java.util.Date

class StepHabitViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStepHabitViewBinding

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                var rHabit = intent?.getSerializableExtra("habit") as StepsHabit
                rHabit.accomplishment = habit.accomplishment
                habit = rHabit
                displayHabitInfo()
            }
        }

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
        binding.btnEditar.setOnClickListener {
            startForResult.launch(Intent(this, StepHabitCreationActivity::class.java)
                .apply {
                    putExtra("habit", habit)
                })
        }

        onBackPressedDispatcher.addCallback(this) {
            val intent = Intent().apply { putExtra("habit", habit) }
            setResult(RESULT_OK, intent)
            finish()
        }
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
        binding.vshHour.text = habit.frequency.hourString()
        binding.vshObj.text = "${habit.objectiveSteps} pasos"

        // Days of notification
        for (not in notificationDays) {
            not.setBackgroundColor(getColor(R.color.gray))
            not.setTextColor(getColor(R.color.black))
        }
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