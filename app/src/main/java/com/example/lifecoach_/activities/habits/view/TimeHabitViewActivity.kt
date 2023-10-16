package com.example.lifecoach_.activities.habits.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.lifecoach_.R
import com.example.lifecoach_.databinding.ActivityTimeHabitViewBinding
import com.example.lifecoach_.model.habits.Accomplishment
import com.example.lifecoach_.model.habits.TimeControlHabit
import java.util.Date

class TimeHabitViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTimeHabitViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimeHabitViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        habit = intent.getSerializableExtra("habit") as TimeControlHabit
        startLists()
        displayHabitInfo()
        manageButtons()
    }

    private lateinit var habit: TimeControlHabit
    private var notificationDays = mutableListOf<TextView>()
    private var accomplishmentDays = mutableListOf<TextView>()

    private fun manageButtons() {
        binding.btnStart.setOnClickListener {
            // TODO : Iniciar actividad de tiempo
            displayHabitInfo()
        }
    }

    private fun startLists() {
        notificationDays = mutableListOf(
            binding.vthDay0,
            binding.vthDay1,
            binding.vthDay2,
            binding.vthDay3,
            binding.vthDay4,
            binding.vthDay5,
            binding.vthDay6
        )

        accomplishmentDays = mutableListOf(
            binding.vthADay0,
            binding.vthADay1,
            binding.vthADay2,
            binding.vthADay3,
            binding.vthADay4,
            binding.vthADay5,
            binding.vthADay6
        )
    }

    private fun displayHabitInfo() {
        binding.vthHabitName.text = habit.name
        binding.vthHour.text = habit.frequency.hourString()

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