package com.example.lifecoach_.activities.habits.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lifecoach_.R
import com.example.lifecoach_.activities.habits.auxiliar.TimeHabitRegisterActivity
import com.example.lifecoach_.activities.habits.creation.RunningHabitCreationActivity
import com.example.lifecoach_.activities.habits.creation.TimeHabitCreationActivity
import com.example.lifecoach_.databinding.ActivityTimeHabitViewBinding
import com.example.lifecoach_.model.habits.RunningHabit
import com.example.lifecoach_.model.habits.TimeControlHabit

class TimeHabitViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTimeHabitViewBinding

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                var rHabit = intent?.getSerializableExtra("habit") as TimeControlHabit
                rHabit.accomplishment = habit.accomplishment
                habit = rHabit
                displayHabitInfo()
            }
        }

    private val startForTimeResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val intent = result.data
                val minutesRanToday = intent?.getIntExtra("minutes", 0)
                habit.setTodayAccomplishment(minutesRanToday!!)
                updateAccomplishment()
            }
        }

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
            intent = Intent(baseContext, TimeHabitRegisterActivity::class.java)
            startForTimeResult.launch(intent)
            displayHabitInfo()
        }

        binding.btnEditar.setOnClickListener {
            startForResult.launch(Intent(this, TimeHabitCreationActivity::class.java)
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
        binding.vthObj.text = "${habit.objectiveMins} minutos"

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