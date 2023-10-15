package com.example.lifecoach_.activities.habits.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.lifecoach_.R
import com.example.lifecoach_.activities.habits.auxiliar.RunningActionHabitActivity
import com.example.lifecoach_.databinding.ActivityRunningHabitViewBinding
import com.example.lifecoach_.model.habits.RunningHabit
import java.util.Date

class RunningHabitViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRunningHabitViewBinding

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
        val hour = "${habit.frequency.notiHour}:${habit.frequency.notiMinute}"
        binding.vrhHour.text = hour

        // Days of notification
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