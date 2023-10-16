package com.example.lifecoach_.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.lifecoach_.R
import com.example.lifecoach_.activities.friends.ChatMenuActivity
import com.example.lifecoach_.activities.habits.CreateHabitsActivity
import com.example.lifecoach_.activities.habits.view.GenericHabitViewActivity
import com.example.lifecoach_.activities.habits.view.MuscularHabitViewActivity
import com.example.lifecoach_.activities.habits.view.RunningHabitViewActivity
import com.example.lifecoach_.activities.habits.view.StepHabitViewActivity
import com.example.lifecoach_.activities.habits.view.TimeHabitViewActivity
import com.example.lifecoach_.adapters.HabitListViewAdapter
import com.example.lifecoach_.databinding.ActivityDashBoardHabitsBinding
import com.example.lifecoach_.model.User
import com.example.lifecoach_.model.habits.Habit
import com.example.lifecoach_.model.habits.RunningHabit
import com.example.lifecoach_.model.habits.StepsHabit
import com.example.lifecoach_.model.habits.StrengthHabit
import com.example.lifecoach_.model.habits.TimeControlHabit

class DashBoardHabitsActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var binding: ActivityDashBoardHabitsBinding
    private lateinit var uriImage: Uri
    private lateinit var userTest: User
    private var todayHabits = mutableListOf<Habit>()
    private var otherHabits = mutableListOf<Habit>()
    private var showToday = true

    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val habit = intent?.getSerializableExtra("habit") as Habit
                Log.i("HABIT", "${habit.name}, ${habit.frequency}")
                userTest.habits.add(habit)
                updateHabits()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardHabitsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Fill the info. with the login activity
        userTest = intent.getSerializableExtra("user") as User

        // Show habits
        updateHabits()

        // Set click listeners
        manageButtons(userTest)

        configureLightSensor(this)
    }

    override fun getTheme(): Resources.Theme {
        val theme = super.getTheme()

        if (userTest.dark_mode == 1) {
            theme.applyStyle(R.style.Base_Theme_LifeCoachDarkTheme, true)
        }

        return theme
    }

    private fun configureLightSensor(sensorEventListener: SensorEventListener) {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        if (lightSensor != null) {
            sensorManager.registerListener(sensorEventListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        // TODO: Handle Sensor Changed
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.i("SENSOR", "Sensor Accurracy Changed")
    }

    private fun updateHabits() {
        todayHabits.clear()
        otherHabits.clear()
        for (habit in userTest.habits) {
            if (habit.shouldDoToday())
                todayHabits.add(habit)
            else
                otherHabits.add(habit)
        }
        if (showToday)
            binding.todayHabits.adapter = HabitListViewAdapter(this, todayHabits)
        else
            binding.todayHabits.adapter = HabitListViewAdapter(this, otherHabits)
    }


    fun manageButtons(user: User) {
        // CREATE HABIT
        binding.createHabitButton.setOnClickListener {
            intent = Intent(baseContext, CreateHabitsActivity::class.java)
            startForResult.launch(intent)
        }

        // ON HABIT CLICK
        binding.todayHabits.setOnItemClickListener { _, _, position, _ ->
            onHabitClick(position)
        }

        // SHOW TODAY HABITS
        binding.showToday.setOnClickListener {
            binding.showOthers.setBackgroundColor(getColor(R.color.gray))
            binding.showOthers.setTextColor(getColor(R.color.black))

            binding.showToday.setBackgroundColor(getColor(R.color.green1))
            binding.showToday.setTextColor(getColor(R.color.white))

            showToday = true
            updateHabits()
        }

        binding.showOthers.setOnClickListener {
            binding.showToday.setBackgroundColor(getColor(R.color.gray))
            binding.showToday.setTextColor(getColor(R.color.black))

            binding.showOthers.setBackgroundColor(getColor(R.color.green1))
            binding.showOthers.setTextColor(getColor(R.color.white))

            showToday = false
            updateHabits()
        }


        bottomNavigationBarManagement(user)
    }

    private fun onHabitClick(position: Int) {
        val intent: Intent
        var habits: MutableList<Habit>
        if (showToday) habits = todayHabits
        else habits = otherHabits

        when (habits[position]) {
            is StrengthHabit -> {
                intent = Intent(baseContext, MuscularHabitViewActivity::class.java)
            }

            is RunningHabit -> {
                intent = Intent(baseContext, RunningHabitViewActivity::class.java)
            }

            is StepsHabit -> {
                intent = Intent(baseContext, StepHabitViewActivity::class.java)
            }

            is TimeControlHabit -> {
                intent = Intent(baseContext, TimeHabitViewActivity::class.java)
            }

            else -> {
                intent = Intent(baseContext, GenericHabitViewActivity::class.java)
            }
        }
        intent.putExtra("habit", habits[position])
        startActivity(intent)
    }

    private fun bottomNavigationBarManagement(user: User) {
        binding.bottomNavigationViewCreate.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuProfile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    intent.putExtra("user", user)
                    startActivity(intent)
                    finish()
                    false
                }

                R.id.menuChat -> {
                    // Do an intent with the chat activity
                    val intent = Intent(this, ChatMenuActivity::class.java)
                    startActivity(intent)
                    false
                }

                else -> false
            }
        }
    }
}