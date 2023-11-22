package com.example.lifecoach_.activities.habits.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.lifecoach_.R
import com.example.lifecoach_.activities.habits.creation.GenericHabitCreationActivity
import com.example.lifecoach_.databinding.ActivityGenericHabitViewBinding
import com.example.lifecoach_.model.habits.Habit

class GenericHabitViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGenericHabitViewBinding

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                var rHabit = intent?.getSerializableExtra("habit") as Habit
                rHabit.accomplishment = habit.accomplishment
                habit = rHabit
                displayHabitInfo()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenericHabitViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        habit = intent.getSerializableExtra("habit") as Habit
        startLists()
        displayHabitInfo()
        manageButtons()
    }

    private lateinit var habit: Habit
    private var notificationDays = mutableListOf<TextView>()
    private var accomplishmentDays = mutableListOf<TextView>()

    private fun manageButtons() {
        binding.btnCompletado.setOnClickListener {
            binding.btnCompletado.setIconTintResource(R.color.white)
            habit.setTodayAccomplishment(1)
            updateAccomplishment()
        }

        binding.btnEditar.setOnClickListener {
            startForResult.launch(Intent(this, GenericHabitCreationActivity::class.java)
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
            binding.vghDay0,
            binding.vghDay1,
            binding.vghDay2,
            binding.vghDay3,
            binding.vghDay4,
            binding.vghDay5,
            binding.vghDay6
        )

        accomplishmentDays = mutableListOf(
            binding.vghADay0,
            binding.vghADay1,
            binding.vghADay2,
            binding.vghADay3,
            binding.vghADay4,
            binding.vghADay5,
            binding.vghADay6
        )
    }

    private fun displayHabitInfo() {
        binding.vghHabitName.text = habit.name
        binding.vghHour.text = habit.frequency.hourString()

        if(habit.doneToday())
            binding.btnCompletado.setIconTintResource(R.color.white)

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
                if (habit.accomplishment[habit.accomplishment.size - 1 - i].accomplishment >= 1)
                    accomplishmentDays[i].setBackgroundColor(getColor(R.color.green1))
                else accomplishmentDays[i].setBackgroundColor(getColor(R.color.red))
            } else break
        }
    }
}