package com.example.lifecoach_.activities

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
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

class DashBoardHabitsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashBoardHabitsBinding
    private lateinit var uriImage: Uri
    private lateinit var userTest: User
    private var todayHabits = mutableListOf<Habit>()
    private var otherHabits = mutableListOf<Habit>()
    private var showToday = true

    private var selectedHabit = 0

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val habit = intent?.getSerializableExtra("habit") as Habit
                userTest.habits.add(habit)
                updateHabits()
            }
        }

    private val startForUpdate =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val habit = intent?.getSerializableExtra("habit") as Habit
                userTest.habits[selectedHabit] = habit
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
            binding.showOthers.backgroundTintList = ColorStateList.valueOf(getColor(R.color.dark_gray))
            binding.showOthers.setTextColor(getColor(R.color.white))

            binding.showToday.backgroundTintList = ColorStateList.valueOf(getColor(R.color.light_gray))
            binding.showToday.setTextColor(getColor(R.color.dark_gray))

            showToday = true
            updateHabits()
        }

        binding.showOthers.setOnClickListener {
            binding.showToday.backgroundTintList = ColorStateList.valueOf(getColor(R.color.dark_gray))
            binding.showToday.setTextColor(getColor(R.color.white))

            binding.showOthers.backgroundTintList = ColorStateList.valueOf(getColor(R.color.light_gray))
            binding.showOthers.setTextColor(getColor(R.color.dark_gray))

            showToday = false
            updateHabits()
        }


        bottomNavigationBarManagement(user)
    }

    private fun onHabitClick(position: Int) {
        val intent: Intent
        val habits: MutableList<Habit>
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
        selectedHabit = userTest.habits.indexOf(habits[position])
        intent.putExtra("habit", habits[position])
        startForUpdate.launch(intent)
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