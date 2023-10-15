package com.example.lifecoach_.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.CallSuper
import androidx.annotation.RequiresApi
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
import java.util.Calendar

class DashBoardHabitsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashBoardHabitsBinding
    private lateinit var uriImage: Uri
    private lateinit var userTest: User
    private var todayHabits = mutableListOf<Habit>()

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
    }

    private fun updateHabits() {
        todayHabits.clear()
        for(habit in userTest.habits){
            if(habit.shouldDoToday())
                todayHabits.add(habit)
        }
        val adapter = HabitListViewAdapter(this, todayHabits)
        binding.habitsListView.adapter = adapter
    }


    fun manageButtons(user: User) {
        // CREATE HABIT
        binding.createHabitButton.setOnClickListener {
            intent = Intent(baseContext, CreateHabitsActivity::class.java)
            startForResult.launch(intent)
        }

        // ON HABIT CLICK
        binding.habitsListView.setOnItemClickListener { parent, view, position, id ->
            val intent: Intent
            when (userTest.habits[position]) {
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
            intent.putExtra("habit", userTest.habits[position])
            startActivity(intent)
        }

        bottomNavigationBarManagement(user)
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