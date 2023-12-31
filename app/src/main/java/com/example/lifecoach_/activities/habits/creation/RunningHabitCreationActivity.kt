package com.example.lifecoach_.activities.habits.creation

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.lifecoach_.R
import com.example.lifecoach_.activities.habits.auxiliar.TimePickerFragment
import com.example.lifecoach_.databinding.ActivityRunningHabitCreationBinding
import com.example.lifecoach_.model.habits.Frequency
import com.example.lifecoach_.model.habits.RunningHabit

class RunningHabitCreationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRunningHabitCreationBinding

    private var days = mutableListOf<TextView>()
    private var selectedDays = mutableListOf<Boolean>()
    private var selectedHour = 0
    private var selectedMin = 0
    private var habit : RunningHabit? = null
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunningHabitCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        manageButtons()

        habit = intent.getSerializableExtra("habit") as RunningHabit?
        if(habit != null)
            displayHabitInfo()
    }

    private fun manageButtons() {
        // Day selection
        days = mutableListOf(
            binding.rhDay0,
            binding.rhDay1,
            binding.rhDay2,
            binding.rhDay3,
            binding.rhDay4,
            binding.rhDay5,
            binding.rhDay6
        )
        for (i in 0 until days.size) {
            selectedDays.add(false)
            days[i].setOnClickListener {
                if (selectedDays[i]) {
                    days[i].setBackgroundColor(getColor(R.color.gray))
                    days[i].setTextColor(getColor(R.color.black))
                    selectedDays[i] = false
                } else {
                    days[i].setBackgroundColor(getColor(R.color.green1))
                    days[i].setTextColor(getColor(R.color.white))
                    selectedDays[i] = true
                }
            }
        }

        // Time selection
        binding.rhTimePick.setOnClickListener {
            showTimePickerDialog()
        }

        // On create habit
        binding.btnCrear.setOnClickListener {
            if (validarForm()) {
                val notiDays = mutableListOf<Int>()
                for (i in 0 until selectedDays.size) if (selectedDays[i]) notiDays.add(i)
                val intent = Intent().apply {
                    putExtra(
                        "habit",
                        RunningHabit(
                            habit.let { it?.id },
                            binding.rhName.text.toString(),
                            Frequency(selectedHour, selectedMin, notiDays),
                            binding.rhObjective.text.toString().toInt()
                        )
                    )
                }
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    private fun displayHabitInfo() {
        binding.rhName.setText(habit!!.name)
        // Days of notification
        for (day in habit!!.frequency.days) {
            days[day].setBackgroundColor(getColor(R.color.green1))
            days[day].setTextColor(getColor(R.color.white))
            selectedDays[day] = true
        }
        binding.rhObjective.setText(habit!!.objectiveMins.toString())
        binding.rhTimePick.setText(habit!!.frequency.hourString())
        selectedHour = habit!!.frequency.notiHour
        selectedMin = habit!!.frequency.notiMinute
    }

    private fun showTimePickerDialog() {
        val timePicker = TimePickerFragment { hour: Int, minute: Int ->
            selectedHour = hour
            selectedMin = minute
            val text = "$hour:$minute"
            binding.rhTimePick.setText(text)
        }
        timePicker.show(supportFragmentManager, "time")
    }

    private fun validarForm(): Boolean {
        if (binding.rhName.text.isEmpty()) {
            binding.rhName.error = "campo necesario"
            return false
        }

        if (binding.rhObjective.text.isEmpty()) {
            binding.rhName.error = "campo necesario"
            return false
        }

        var days = 0
        for (day in selectedDays) if (day) days++
        if (days == 0) {
            Toast.makeText(baseContext, "Es necesario elegir almenos un dia", Toast.LENGTH_LONG)
                .show()
            return false
        }
        if (binding.rhTimePick.text.isEmpty()) {
            binding.rhName.error = "campo necesario"
            return false
        }
        return true
    }
}