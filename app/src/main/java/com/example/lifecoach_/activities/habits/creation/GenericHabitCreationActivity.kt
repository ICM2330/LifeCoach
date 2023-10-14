package com.example.lifecoach_.activities.habits.creation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.lifecoach_.R
import com.example.lifecoach_.activities.habits.auxiliar.TimePickerFragment
import com.example.lifecoach_.databinding.ActivityGenericHabitCreationBinding
import com.example.lifecoach_.model.habits.Frequency
import com.example.lifecoach_.model.habits.Habit
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK
import com.google.android.material.timepicker.TimeFormat

class GenericHabitCreationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGenericHabitCreationBinding
    private var days = mutableListOf<TextView>()
    private var selectedDays = mutableListOf<Boolean>()
    private var selectedHour = 0
    private var selectedMin = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenericHabitCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        days = mutableListOf(
            binding.ghDay0,
            binding.ghDay1,
            binding.ghDay2,
            binding.ghDay3,
            binding.ghDay4,
            binding.ghDay5,
            binding.ghDay6
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

        binding.ghTimePick.setOnClickListener {
            showTimePickerDialog()
        }

        binding.btnCrear.setOnClickListener {
            if (validarForm()) {
                val notiDays = mutableListOf<Int>()
                for (i in 0 until selectedDays.size) if (selectedDays[i]) notiDays.add(i)
                val intent = Intent().apply {
                    putExtra(
                        "habit",
                        Habit(
                            binding.ghName.text.toString(),
                            Frequency(selectedHour, selectedMin, notiDays)
                        )
                    )
                }
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    private fun showTimePickerDialog() {
        val timePicker = TimePickerFragment { hour: Int, minute: Int ->
            selectedHour = hour
            selectedMin = minute
            val text = "$hour:$minute"
            binding.ghTimePick.setText(text)
        }
        timePicker.show(supportFragmentManager, "time")
    }

    private fun validarForm(): Boolean {
        if (binding.ghName.text.isEmpty()) {
            binding.ghName.error = "campo necesario"
            return false
        }

        var days = 0
        for (day in selectedDays) if (day) days++
        if (days == 0)
            Toast.makeText(baseContext, "Es necesario elegir almenos un dia", Toast.LENGTH_LONG)
                .show()

        if (binding.ghTimePick.text.isEmpty()) {
            binding.ghName.error = "campo necesario"
            return false
        }
        return true
    }
}