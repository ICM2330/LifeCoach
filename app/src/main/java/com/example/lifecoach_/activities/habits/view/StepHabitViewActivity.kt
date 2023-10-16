package com.example.lifecoach_.activities.habits.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.lifecoach_.R
import com.example.lifecoach_.databinding.ActivityStepHabitViewBinding
import com.example.lifecoach_.model.habits.Accomplishment
import com.example.lifecoach_.model.habits.StepsHabit
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
        binding.vshHour.text = habit.frequency.hourString()

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