package com.example.lifecoach_.activities.habits.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.lifecoach_.R
import com.example.lifecoach_.activities.habits.auxiliar.RunningActionHabitActivity
import com.example.lifecoach_.activities.habits.creation.GenericHabitCreationActivity
import com.example.lifecoach_.activities.habits.creation.RunningHabitCreationActivity
import com.example.lifecoach_.databinding.ActivityRunningHabitViewBinding
import com.example.lifecoach_.model.habits.Habit
import com.example.lifecoach_.model.habits.RunningHabit
import java.util.Date

class RunningHabitViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRunningHabitViewBinding

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                var rHabit = intent?.getSerializableExtra("habit") as RunningHabit
                rHabit.accomplishment = habit.accomplishment
                habit = rHabit
                displayHabitInfo()
            }
        }

    private val startForResultGetDistance =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val intent = result.data
                val minutesRanToday = intent?.getIntExtra("minutesRan", 0)
                // Show the minutes ran today
                Log.i("HABIT", "Minutes ran today: $minutesRanToday")

                habit.setTodayAccomplishment(minutesRanToday!!)
                updateAccomplishment()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunningHabitViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        habit = intent.getSerializableExtra("habit") as RunningHabit
        startLists()
        displayHabitInfo()
        manageButtons()
    }

    private lateinit var habit: RunningHabit
    private var notificationDays = mutableListOf<TextView>()
    private var accomplishmentDays = mutableListOf<TextView>()

    private fun manageButtons() {
        binding.btnStart.setOnClickListener {
            intent = Intent(baseContext, RunningActionHabitActivity::class.java)
            startForResultGetDistance.launch(intent)
            displayHabitInfo()
        }

        binding.btnEditar.setOnClickListener {
            startForResult.launch(Intent(this, RunningHabitCreationActivity::class.java)
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
            binding.vrhDay0,
            binding.vrhDay1,
            binding.vrhDay2,
            binding.vrhDay3,
            binding.vrhDay4,
            binding.vrhDay5,
            binding.vrhDay6
        )

        accomplishmentDays = mutableListOf(
            binding.vrhADay0,
            binding.vrhADay1,
            binding.vrhADay2,
            binding.vrhADay3,
            binding.vrhADay4,
            binding.vrhADay5,
            binding.vrhADay6
        )
    }

    private fun displayHabitInfo() {
        binding.vrhHabitName.text = habit.name
        binding.vrhHour.text = habit.frequency.hourString()
        binding.vrhObj.text = "${habit.objectiveMins} minutos"

        // Days of notification
        for (not in notificationDays) {
            not.setBackgroundColor(getColor(R.color.gray))
            not.setTextColor(getColor(R.color.black))
        }
        for (day in habit.frequency.days) {
            notificationDays[day].setBackgroundColor(getColor(R.color.green1))
            notificationDays[day].setTextColor(getColor(R.color.white))
        }
        updateAccomplishment()
    }

    private fun updateAccomplishment() {
        for (i in 0..6) {
            if (habit.accomplishment.size > i) {
                if (habit.accomplishment[habit.accomplishment.size - 1 - i].accomplishment >= habit.objectiveMins)
                    accomplishmentDays[i].setBackgroundColor(getColor(R.color.green1))
                else accomplishmentDays[i].setBackgroundColor(getColor(R.color.red))
            } else break
        }
    }
}