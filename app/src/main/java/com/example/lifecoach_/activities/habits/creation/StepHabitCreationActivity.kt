package com.example.lifecoach_.activities.habits.creation

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lifecoach_.R
import com.example.lifecoach_.activities.habits.auxiliar.TimePickerFragment
import com.example.lifecoach_.databinding.ActivityStepHabitCreationBinding
import com.example.lifecoach_.model.habits.Frequency
import com.example.lifecoach_.model.habits.StepsHabit

class StepHabitCreationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStepHabitCreationBinding

    private var days = mutableListOf<TextView>()
    private var selectedDays = mutableListOf<Boolean>()
    private var selectedHour = 0
    private var selectedMin = 0
    private var habit : StepsHabit? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStepHabitCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        manageButtons()

        habit = intent.getSerializableExtra("habit") as StepsHabit?
        if(habit != null)
            displayHabitInfo()
    }

    private fun manageButtons() {
        // Day selection
        days = mutableListOf(
            binding.shDay0,
            binding.shDay1,
            binding.shDay2,
            binding.shDay3,
            binding.shDay4,
            binding.shDay5,
            binding.shDay6
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
        binding.shTimePick.setOnClickListener {
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
                        StepsHabit(
                            habit.let { it?.id },
                            binding.shName.text.toString(),
                            Frequency(selectedHour, selectedMin, notiDays),
                            binding.shObjective.text.toString().toInt()
                        )
                    )
                }
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    private fun displayHabitInfo() {
        binding.shName.setText(habit!!.name)
        // Days of notification
        for (day in habit!!.frequency.days) {
            days[day].setBackgroundColor(getColor(R.color.green1))
            days[day].setTextColor(getColor(R.color.white))
            selectedDays[day] = true
        }
        binding.shTimePick.setText(habit!!.frequency.hourString())
        binding.shObjective.setText(habit!!.objectiveSteps.toString())
        selectedHour = habit!!.frequency.notiHour
        selectedMin = habit!!.frequency.notiMinute
    }

    private fun showTimePickerDialog() {
        val timePicker = TimePickerFragment { hour: Int, minute: Int ->
            selectedHour = hour
            selectedMin = minute
            val text = "$hour:$minute"
            binding.shTimePick.setText(text)
        }
        timePicker.show(supportFragmentManager, "time")
    }

    private fun validarForm(): Boolean {
        if (binding.shName.text.isEmpty()) {
            binding.shName.error = "campo necesario"
            return false
        }

        if (binding.shObjective.text.isEmpty()) {
            binding.shName.error = "campo necesario"
            return false
        }

        var days = 0
        for (day in selectedDays) if (day) days++
        if (days == 0) {
            Toast.makeText(baseContext, "Es necesario elegir almenos un dia", Toast.LENGTH_LONG)
                .show()
            return false
        }
        if (binding.shTimePick.text.isEmpty()) {
            binding.shName.error = "campo necesario"
            return false
        }
        return true
    }
}