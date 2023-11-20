package com.example.lifecoach_.activities.habits.creation

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lifecoach_.R
import com.example.lifecoach_.activities.habits.auxiliar.TimePickerFragment
import com.example.lifecoach_.databinding.ActivityMuscularHabitCreationBinding
import com.example.lifecoach_.model.habits.Frequency
import com.example.lifecoach_.model.habits.StrengthHabit

class MuscularHabitCreationActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMuscularHabitCreationBinding
    private var days = mutableListOf<TextView>()
    private var selectedDays = mutableListOf<Boolean>()
    private var selectedHour = 0
    private var selectedMin = 0
    private var habit : StrengthHabit? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMuscularHabitCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        days = mutableListOf(
            binding.mhDay0,
            binding.mhDay1,
            binding.mhDay2,
            binding.mhDay3,
            binding.mhDay4,
            binding.mhDay5,
            binding.mhDay6
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

        habit = intent.getSerializableExtra("habit") as StrengthHabit?
        if(habit != null)
            displayHabitInfo()

        binding.mhTimePick.setOnClickListener {
            showTimePickerDialog()
        }

        binding.btnCrear.setOnClickListener {
            if (validarForm()) {
                val notiDays = mutableListOf<Int>()
                for (i in 0 until selectedDays.size) if (selectedDays[i]) notiDays.add(i)
                val intent = Intent().apply {
                    putExtra(
                        "habit",
                        StrengthHabit(
                            null,
                            binding.mhName.text.toString(),
                            Frequency(selectedHour, selectedMin, notiDays),
                            binding.mhMuscularGroup.text.toString()
                        )
                    )
                }
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    private fun displayHabitInfo() {
        binding.mhName.setText(habit!!.name)
        // Days of notification
        for (day in habit!!.frequency.days) {
            days[day].setBackgroundColor(getColor(R.color.green1))
            days[day].setTextColor(getColor(R.color.white))
            selectedDays[day] = true
        }
        binding.mhMuscularGroup.setText(habit!!.muscularGroup)
        binding.mhTimePick.setText(habit!!.frequency.hourString())
        selectedHour = habit!!.frequency.notiHour
        selectedMin = habit!!.frequency.notiMinute
    }

    private fun showTimePickerDialog() {
        val timePicker = TimePickerFragment { hour: Int, minute: Int ->
            selectedHour = hour
            selectedMin = minute
            val text = "$hour:$minute"
            binding.mhTimePick.setText(text)
        }
        timePicker.show(supportFragmentManager, "time")
    }

    private fun validarForm(): Boolean {
        if (binding.mhName.text.isEmpty()) {
            binding.mhName.error = "campo necesario"
            return false
        }

        if (binding.mhMuscularGroup.text.isEmpty()) {
            binding.mhMuscularGroup.error = "campo necesario"
            return false
        }

        var days = 0
        for (day in selectedDays) if (day) days++
        if (days == 0) {
            Toast.makeText(baseContext, "Es necesario elegir almenos un dia", Toast.LENGTH_LONG)
                .show()
            return false
        }
        if (binding.mhTimePick.text.isEmpty()) {
            binding.mhName.error = "campo necesario"
            return false
        }
        return true
    }
    }

