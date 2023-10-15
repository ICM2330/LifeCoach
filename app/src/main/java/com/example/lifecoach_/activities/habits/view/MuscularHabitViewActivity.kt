package com.example.lifecoach_.activities.habits.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.lifecoach_.R
import com.example.lifecoach_.activities.habits.auxiliar.SearchGymForHabitActivity
import com.example.lifecoach_.databinding.ActivityMuscularHabitViewBinding
import com.example.lifecoach_.model.habits.Accomplishment
import com.example.lifecoach_.model.habits.StrengthHabit
import java.util.Date

class MuscularHabitViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMuscularHabitViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMuscularHabitViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        habit = intent.getSerializableExtra("habit") as StrengthHabit
        startLists()
        displayHabitInfo()
        manageButtons()
    }

    private lateinit var habit: StrengthHabit
    private var notificationDays = mutableListOf<TextView>()
    private var accomplishmentDays = mutableListOf<TextView>()

    private fun manageButtons() {

        // SEARCH NEAR GYMS
        binding.buttonGimnasiosSearch.setOnClickListener {
            startActivity(Intent(baseContext, SearchGymForHabitActivity::class.java))
        }

        // TODAY FEELING
        binding.btnGood.setOnClickListener {
            habit.accomplishment[habit.accomplishment.size-1].accomplishment = 2
            updateAccomplishment()
        }

        binding.btnMeh.setOnClickListener {
            habit.accomplishment[habit.accomplishment.size-1].accomplishment = 1
            updateAccomplishment()
        }

        binding.btnBad.setOnClickListener {
            habit.accomplishment[habit.accomplishment.size-1].accomplishment = 0
            updateAccomplishment()
        }
    }

    private fun startLists() {
        notificationDays = mutableListOf(
            binding.vmhDay0,
            binding.vmhDay1,
            binding.vmhDay2,
            binding.vmhDay3,
            binding.vmhDay4,
            binding.vmhDay5,
            binding.vmhDay6
        )

        accomplishmentDays = mutableListOf(
            binding.vmhADay0,
            binding.vmhADay1,
            binding.vmhADay2,
            binding.vmhADay3,
            binding.vmhADay4,
            binding.vmhADay5,
            binding.vmhADay6
        )
    }

    private fun displayHabitInfo() {
        binding.vmhHabitName.text = habit.name
        val hour = "${habit.frequency.notiHour}:${habit.frequency.notiMinute}"
        binding.vmhHour.text = hour

        // Days of notification
        for (day in habit.frequency.days) {
            notificationDays[day].setBackgroundColor(getColor(R.color.green1))
            notificationDays[day].setTextColor(getColor(R.color.white))
        }

        habit.accomplishment.clear()
        habit.accomplishment.add(Accomplishment(Date(), 0))
        habit.accomplishment.add(Accomplishment(Date(), 1))
        habit.accomplishment.add(Accomplishment(Date(), 2))

        updateAccomplishment()
    }

    private fun updateAccomplishment() {
        for (i in 0..6) {
            if (habit.accomplishment.size > i) {
                if (habit.accomplishment[habit.accomplishment.size - 1 - i].accomplishment == 2)
                    accomplishmentDays[i].setBackgroundColor(getColor(R.color.green1))
                else if (habit.accomplishment[habit.accomplishment.size - 1 - i].accomplishment == 1)
                    accomplishmentDays[i].setBackgroundColor(getColor(R.color.orange))
                else accomplishmentDays[i].setBackgroundColor(getColor(R.color.red))
            } else break
        }
    }
}